package com.charroux.publisher;

import com.charroux.carservice.entity.Car;
import com.charroux.carservice.entity.Event;
import com.charroux.carservice.entity.RentalAgreement;
import com.charroux.carstat.entity.Customer;
import com.charroux.carstat.entity.CustomerApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.Producer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootApplication
public class PublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			try {

				Environment environment = Environment.builder().uri("rabbitmq-stream://localhost:5552").build();
				environment.streamCreator().stream("first-application-stream").create();
				Producer producer = environment.producerBuilder().stream("first-application-stream").build();
				long start = System.currentTimeMillis();
				int messageCount = 100;
				CountDownLatch confirmLatch = new CountDownLatch(messageCount);

				RentalAgreement rentalAgreement = new RentalAgreement(1, RentalAgreement.State.PENDING);
				rentalAgreement.setId(1);
				Car car = new Car("11AA22", "Ferrari", 1000);
				car.setId(1);
				rentalAgreement.addCar(car);

				Event event = new Event("RentalAgreement", rentalAgreement.getId(), "RentalAgreement.State");
				event.setId(1);
				event.setData(rentalAgreement);

				ObjectMapper objectMapper = new ObjectMapper();

				byte[] data = objectMapper.writeValueAsBytes(event);
				//Event event1 = objectMapper.readValue(data, Event.class);

				//System.out.println(event1);

				Message message = producer
					.messageBuilder()
					.properties()
					.creationTime(System.currentTimeMillis())
					.messageId(1)
					.messageBuilder()
					.addData(data)
					.build();

				producer.send(message, confirmationStatus -> confirmLatch.countDown());
				System.out.println("sent");

				CustomerApplication customerApplication = new CustomerApplication(CustomerApplication.State.CREDIT_REJECTED);
				customerApplication.setId(1);

				Customer customer = new Customer(1000);
				customer.setId(1);

				customerApplication.setCustomer(customer);

				event = new Event("CustomerApplication", customerApplication.getId(), "CustomerApplication.State");
				event.setId(2);
				event.setData(customerApplication);

				objectMapper = new ObjectMapper();

				data = objectMapper.writeValueAsBytes(event);

				message = producer
						.messageBuilder()
						.properties()
						.creationTime(System.currentTimeMillis())
						.messageId(2)
						.messageBuilder()
						.addData(data)
						.build();

				producer.send(message, confirmationStatus -> confirmLatch.countDown());
				System.out.println("sent");

				try {
					boolean done = confirmLatch.await(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	/*
	private static void run(String... args) {

		Environment environment = Environment.builder().uri("rabbitmq-stream://localhost:5552").build();
		environment.streamCreator().stream("first-application-stream").create();
		Producer producer = environment.producerBuilder().stream("first-application-stream").build();
		long start = System.currentTimeMillis();
		int messageCount = 100;
		CountDownLatch confirmLatch = new CountDownLatch(messageCount);
		IntStream.range(0, messageCount)
			.forEach(
					i -> {
						Message message = producer
								.messageBuilder()
								.properties()
								.creationTime(System.currentTimeMillis())
								.messageId(i)
								.messageBuilder()
								.addData("hello world".getBytes(StandardCharsets.UTF_8))
								.build();
						producer.send(message, confirmationStatus -> confirmLatch.countDown());
						System.out.println("sent");
					});
		try {
			boolean done = confirmLatch.await(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	};*/



}
