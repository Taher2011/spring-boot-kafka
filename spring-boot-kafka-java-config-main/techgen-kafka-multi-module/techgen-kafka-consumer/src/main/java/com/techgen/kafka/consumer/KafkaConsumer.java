package com.techgen.kafka.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.techgen.kafka.model.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaConsumer {

	@Value("${spring.kafka.topic}")
	private String topic;

	@KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeMessage(User user) {
		log.info("consumed message {} from topic {} ", user, topic);
	}

}
