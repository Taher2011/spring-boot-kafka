package com.techgen.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

	@Value("${spring.kafka.topic}")
	private String topic;

	@Bean
	public NewTopic newTopic() {
		return TopicBuilder.name(topic).partitions(3).build();
	}

}
