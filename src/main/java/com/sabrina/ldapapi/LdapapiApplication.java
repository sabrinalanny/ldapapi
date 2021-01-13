package com.sabrina.ldapapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Sabrina
 *
 */
@SpringBootApplication
@ComponentScan(basePackages="com.sabrina.ldapapi")
public class LdapapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdapapiApplication.class, args);
	}

}
