package com.techgen.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.techgen.kafka.model.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaProducer {

	@Value("${spring.kafka.topic}")
	private String topic;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void produceMessage() {
		User user = new User("Taher", "Ali");
		for (int i = 1; i <= 10; i++) {
			kafkaTemplate.send(topic, user);
		}
		log.info("produced message to topic {} ", topic);
	}

}
