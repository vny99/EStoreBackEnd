package com.store.backend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.validation.Validator;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableWebFlux
@OpenAPIDefinition
public class EStoreBackendApplication {

	@Bean
	WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}
	
	@Bean
	 ConnectionFactoryInitializer initializer(
			@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		ResourceDatabasePopulator resource = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
		initializer.setDatabasePopulator(resource);
		return initializer;
	}
	
	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	public static void main(String[] args) {
		SpringApplication.run(EStoreBackendApplication.class, args);
	}

}
