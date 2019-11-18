package es.codeurjc.test.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebApp {

	@Value("${mysql.db.host}")
	String mysqlDbHost;

	private static ConfigurableApplicationContext app;

	public static void main(String[] args) throws Exception {
		System.out.println("MySql host: " + mysqlDbHost);
		SpringApplication.run(WebApp.class, args);
	}

	public static void start() {
		start(new String[] {});
	}

	private static void start(String[] args) {
		if (app == null) {
			app = SpringApplication.run(WebApp.class, args);
		}
	}

	public static void stop() {
		if (app != null) {
			app.close();
		}
	}
}
