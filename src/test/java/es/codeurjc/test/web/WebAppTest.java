package es.codeurjc.test.web;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebAppTest {
    protected final Logger logger = getLogger(lookup().lookupClass());

    private static Logger LOG = LoggerFactory.getLogger(WebAppTest.class);

    private static String sutURL;
    private static String eusURL;

    private WebDriver driver;

    @BeforeAll
    public static void setupClass() throws Exception {

        String sutHost = System.getenv("ET_SUT_HOST");
        if (sutHost == null) {
            sutURL = "http://localhost:38080/";
        } else {
            sutURL = "http://" + sutHost + ":38080/";
        }
        System.out.println("App url: " + sutURL);
        
        waitForSut(sutURL);

        eusURL = System.getenv("ET_EUS_API");
        if (eusURL == null) {
            WebDriverManager.chromedriver().setup();
        }
    }

    @BeforeEach
    public void setupTest(TestInfo info) throws MalformedURLException {
        String testName = info.getTestMethod().get().getName();
        logger.info("##### Start test: {}", testName);

        String eusURL = System.getenv("ET_EUS_API");
        if (eusURL == null) {
            // Local Google Chrome
            driver = new ChromeDriver();
        } else {
            DesiredCapabilities caps = chrome();
            caps.setCapability("testName", testName);

            // Selenium Grid in ElasTest
            driver = new RemoteWebDriver(new URL(eusURL), caps);
        }
    }

    @AfterEach
    public void teardown(TestInfo info) {
        if (driver != null) {
            driver.quit();
        }
        String testName = info.getTestMethod().get().getName();
        logger.info("##### Finish test: {}", testName);
    }



    @Test
    public void createMessageTest() throws InterruptedException {
        Thread.sleep(1000);
        driver.get(sutURL);
        LOG.info("Web loaded");
        Thread.sleep(3000);

        String newTitle = "MessageTitle";
        String newBody = "MessageBody";

        addMessage(newTitle, newBody);

        String title = driver.findElement(By.id("title")).getText();
        String body = driver.findElement(By.id("body")).getText();

        assertThat(title).isEqualTo(newTitle);
        assertThat(body).isEqualTo(newBody);
        LOG.info("Message verified");

        Thread.sleep(2000);
    }
    
    @Test
    public void removeMessageTest() throws InterruptedException {

        driver.get(sutURL);
        LOG.info("Web loaded");
        Thread.sleep(2000);

        String newTitle = "MessageTitleToBeDeleted";
        String newBody = "MessageBodyToBeDeleted";

        addMessage(newTitle, newBody);

        driver.findElement(By.id("delete")).click();
        LOG.info("Delete button clicked");
        Thread.sleep(2000);

        try {
            driver.findElement(
                    By.xpath("//span[contains(text(),'" + newTitle + "')]"));
            fail("Message should be deleted");
        } catch (Exception e) {
            LOG.info("Message deleted");
        }
    }

    private void addMessage(String title, String body)
            throws InterruptedException {

        driver.findElement(By.id("title-input")).sendKeys(title);
        driver.findElement(By.id("body-input")).sendKeys(body);
        LOG.info("Form ready");

        Thread.sleep(2000);

        driver.findElement(By.id("submit")).click();
        LOG.info("Form submited");

        Thread.sleep(2000);

    }

    public static void waitForSut(String urlValue) throws IOException {
        URL url = new URL(urlValue);
        int responseCode = 0;
        boolean urlIsUp = false;
        
        while (!urlIsUp) {
            LOG.debug("SUT {} is not ready yet", sutURL);
            try {
                Thread.sleep(2000);        
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setConnectTimeout(2000);
                responseCode = huc.getResponseCode();
                urlIsUp = ((responseCode >= 200 && responseCode <= 299)
                        || (responseCode >= 400 && responseCode <= 415));
            } catch (IOException | IllegalArgumentException | InterruptedException e) {
                urlIsUp = false;
            }
        }
    }

}
