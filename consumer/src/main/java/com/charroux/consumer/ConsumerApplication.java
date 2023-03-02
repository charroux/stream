package com.charroux.consumer;

import com.charroux.carservice.entity.Car;
import com.charroux.carservice.entity.Event;
import com.charroux.carservice.entity.RentalAgreement;
import com.charroux.carstat.entity.CustomerApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	public void method(Event event) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		switch (event.getEntityType()){
			case "RentalAgreement":
				RentalAgreement rentalAgreement = objectMapper.convertValue(event.getData(), new TypeReference<RentalAgreement>() {});
				System.out.println("rentalAgreement = " + rentalAgreement);
				rentalAgreement.getCars().stream().forEach(car -> System.out.println(car));
				break;
			case "CustomerApplication":
				CustomerApplication customerApplication = objectMapper.convertValue(event.getData(), new TypeReference<CustomerApplication>() {});
				System.out.println("consumerApplication = " + customerApplication);
				break;
			default:
		}

	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			Environment environment = Environment.builder().uri("rabbitmq-stream://localhost:5552").build();
			AtomicInteger messageConsumed = new AtomicInteger(0);
			long start = System.currentTimeMillis();

			//Set<Event> bodies = ConcurrentHashMap.newKeySet(10);

			ObjectMapper objectMapper = new ObjectMapper();

			Consumer consumer = environment.consumerBuilder().stream("first-application-stream")
					.offset(OffsetSpecification.first())
					.messageHandler((context, message) -> {
						try {
							method(objectMapper.readValue(message.getBodyAsBinary(), Event.class));
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					})
/*							(context, message) -> {
								Event event = null;
								try {
									event = objectMapper.readValue(message.getBodyAsBinary(), Event.class);
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
								System.out.println(event);
							})*/
					.build();

			Thread.sleep(5000);
			// Utils.waitAtMost(60, () -> messageConsumed.get() >= 1000);
		};
	}

}
