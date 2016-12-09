package com.tnmnet.steel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MspsApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MspsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MspsApplication.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8081);
	}
}
