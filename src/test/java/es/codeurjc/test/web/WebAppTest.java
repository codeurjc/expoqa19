package es.codeurjc.test.web;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebAppTest {
	protected static final Logger logger = getLogger(lookup().lookupClass());

	private static String sutURL;

	private WebDriver driver;

	@BeforeAll
	public static void setupClass() throws IOException {
		String sutHost = System.getenv("ET_SUT_HOST");
		if (sutHost == null) {
			sutURL = "http://localhost:38080/";
		} else {
			sutURL = "http://" + sutHost + ":38080/";
		}
		logger.info("App url: " + sutURL);

		waitForSut(sutURL);

		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void setupTest() {
		driver = new ChromeDriver();
	}

	@AfterEach
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void createMessageTest() throws InterruptedException {
		Thread.sleep(1000);
		driver.get(sutURL);
		logger.info("Web loaded");
		Thread.sleep(3000);

		String newTitle = "MessageTitle";
		String newBody = "MessageBody";

		addMessage(newTitle, newBody);

		String title = driver.findElement(By.id("title")).getText();
		String body = driver.findElement(By.id("body")).getText();

		assertThat(title).isEqualTo(newTitle);
		assertThat(body).isEqualTo(newBody);
		logger.info("Message verified");

		Thread.sleep(2000);
	}

	@Test
	public void removeMessageTest() throws InterruptedException {

		driver.get(sutURL);
		logger.info("Web loaded");
		Thread.sleep(2000);

		String newTitle = "MessageTitleToBeDeleted";
		String newBody = "MessageBodyToBeDeleted";

		addMessage(newTitle, newBody);

		driver.findElement(By.id("delete")).click();
		logger.info("Delete button clicked");
		Thread.sleep(2000);

		try {
			driver.findElement(By.xpath("//span[contains(text(),'" + newTitle + "')]"));
			fail("Message should be deleted");
		} catch (Exception e) {
			logger.info("Message deleted");
		}
	}

	private void addMessage(String title, String body) throws InterruptedException {

		driver.findElement(By.id("title-input")).sendKeys(title);
		driver.findElement(By.id("body-input")).sendKeys(body);
		logger.info("Form ready");

		Thread.sleep(2000);

		driver.findElement(By.id("submit")).click();
		logger.info("Form submited");

		Thread.sleep(2000);

	}

	public static void waitForSut(String urlValue) throws IOException {
		URL url = new URL(urlValue);
		int responseCode = 0;
		boolean urlIsUp = false;

		while (!urlIsUp) {
			logger.debug("SUT {} is not ready yet", sutURL);
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
