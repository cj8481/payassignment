package com.kakaopay.recruit.assignment.urlshortening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
public class UrlShoteningApplication extends AbstractReactiveMongoConfiguration {
	@Autowired
	private Environment environment;

	@Override
	@Bean
	@DependsOn("embeddedMongoServer")
	public MongoClient reactiveMongoClient() {
		int port = environment.getProperty("local.mongo.port", Integer.class);
		return MongoClients.create(String.format("mongodb://localhost:%d", port));
	}

	@Override
	protected String getDatabaseName() {
		return "urlShortener";
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlShoteningApplication.class, args);
	}
}
