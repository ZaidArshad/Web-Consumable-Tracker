package ca.cmpt213.a4.webappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main activity for the server to use Spring Boot to run the server
 */
@SpringBootApplication
public class WebAppServerCtApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebAppServerCtApplication.class, args);
	}

}
