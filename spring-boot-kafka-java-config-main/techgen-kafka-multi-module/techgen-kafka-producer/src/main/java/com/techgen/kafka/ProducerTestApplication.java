package com.techgen.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techgen.kafka.producer.KafkaProducer;

@SpringBootApplication
public class ProducerTestApplication implements CommandLineRunner {

	@Autowired
	private KafkaProducer kafkaProducer;

	public static void main(String[] args) {
		SpringApplication.run(ProducerTestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		kafkaProducer.produceMessage();
	}

}
