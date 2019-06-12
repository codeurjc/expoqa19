package es.codeurjc.test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebAppTest {

	private static String sutURL;
	private static String eusURL;

	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {

		String sutHost = System.getenv("ET_SUT_HOST");
		if (sutHost == null) {
			sutURL = "http://localhost:8080/";
		} else {
			sutURL = "http://" + sutHost + ":8080/";
		}
		System.out.println("App url: " + sutURL);

		eusURL = System.getenv("ET_EUS_API");
		if (eusURL == null) {
			WebDriverManager.chromedriver().setup();
		}
	}

	@Before
	public void setupTest() throws MalformedURLException {
		String eusURL = System.getenv("ET_EUS_API");
		if (eusURL == null) {
			// Local Google Chrome
			driver = new ChromeDriver();
		} else {
			// Selenium Grid in ElasTest
			driver = new RemoteWebDriver(new URL(eusURL), chrome());
		}
	}

	@After
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void createMessageTest() throws InterruptedException {

		driver.get(sutURL);
		System.out.println("Web loaded");
		Thread.sleep(2000);

		String newTitle = "MessageTitle";
		String newBody = "MessageBody";

		addMessage(newTitle, newBody);

		String title = driver.findElement(By.id("title")).getText();
		String body = driver.findElement(By.id("body")).getText();

		assertThat(title).isEqualTo(newTitle);
		assertThat(body).isEqualTo(newBody);
		System.out.println("Message verified");

		Thread.sleep(2000);
	}

	@Test
	public void removeMessageTest() throws InterruptedException {

		driver.get(sutURL);
		System.out.println("Web loaded");
		Thread.sleep(2000);

		String newTitle = "MessageTitleToBeDeleted";
		String newBody = "MessageBodyToBeDeleted";

		addMessage(newTitle, newBody);

		driver.findElement(By.id("delete")).click();
		System.out.println("Delete button clicked");
		Thread.sleep(2000);

		try {
			driver.findElement(By.xpath("//span[contains(text(),'"+newTitle+"')]"));
			fail("Message should be deleted");
		} catch(Exception e){
			System.out.println("Message deleted");	
		}
	}

	private void addMessage(String title, String body) throws InterruptedException {

		driver.findElement(By.id("title-input")).sendKeys(title);
		driver.findElement(By.id("body-input")).sendKeys(body);
		System.out.println("Form ready");

		Thread.sleep(2000);

		driver.findElement(By.id("submit")).click();
		System.out.println("Form submited");

		Thread.sleep(2000);

	}

}
