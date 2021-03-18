package com.lambdaschool.oktafoundation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Main class to start the application.
 */
@EnableJpaAuditing
@SpringBootApplication
//@PropertySource(value = "~/oktajavaconfig.properties", ignoreResourceNotFound = true)
public class OktaFoundationApplication {

	/**
	 * Main method to start the application.
	 *
	 * @param args Not used in this application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(OktaFoundationApplication.class, args);
	}

}
