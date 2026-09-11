package org.ironlog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IronlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(IronlogApplication.class, args);
	}

}
