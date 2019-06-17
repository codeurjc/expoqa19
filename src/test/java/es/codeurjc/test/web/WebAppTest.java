package es.codeurjc.test.web;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebAppTest {
    protected final static Logger LOG = getLogger(lookup().lookupClass());

    private static String sutURL;
    private static String eusURL;

    private WebDriver driver;

    @BeforeAll
    public static void setupClass() throws InterruptedException {

        String sutHost = System.getenv("ET_SUT_HOST");
        if (sutHost == null) {
            sutURL = "http://localhost:38080/";
        } else {
            sutURL = "http://" + sutHost + ":38080/";
        }
        LOG.info("App url: " + sutURL);

        eusURL = System.getenv("ET_EUS_API");
        if (eusURL == null) {
            WebDriverManager.chromedriver().setup();
        }

        // Wait for SuT ready
        try {
            while (!checkIfUrlIsUp(sutURL)) {
                LOG.debug("SUT {} is not ready yet", sutURL);
                Thread.sleep(1500);
            }
        }catch (Exception e) {
            
        }
    }

    @BeforeEach
    public void setupTest(TestInfo info) throws MalformedURLException {
        String testName = info.getTestMethod().get().getName();
        LOG.info("##### Start test: {}", testName);

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
        LOG.info("##### Finish test: {}", testName);
    }
    
    public static boolean checkIfUrlIsUp(String urlValue) throws IOException {
        URL url = new URL(urlValue);
        int responseCode = 0;

        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setConnectTimeout(2000);
            responseCode = huc.getResponseCode();
            return ((responseCode >= 200 && responseCode <= 299)
                    || (responseCode >= 400 && responseCode <= 415));
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    @Test
    public void createMessageTest()
            throws InterruptedException, MalformedURLException {
        driver.get(sutURL);
        LOG.info("Web loaded");
        Thread.sleep(3000);

        String newTitle = "MessageTitle";
        String newBody = "MessageBody";

        addMessage(newTitle, newBody);

        String title = driver.findElement(By.id("title")).getText();
        String body = driver.findElement(By.id("body")).getText();

        assertEquals(newTitle, title);
        assertEquals(newBody, body);
        LOG.info("Message verified");

        Thread.sleep(2000);
    }

    @Test
    public void removeMessageTest()
            throws InterruptedException, MalformedURLException {
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

}
