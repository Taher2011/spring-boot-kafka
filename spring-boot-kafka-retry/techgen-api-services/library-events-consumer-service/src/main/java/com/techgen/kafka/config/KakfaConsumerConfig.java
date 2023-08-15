package com.techgen.kafka.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KakfaConsumerConfig {

	private final KafkaProperties properties;

	public KakfaConsumerConfig(KafkaProperties properties) {
		this.properties = properties;
	}

	public DefaultErrorHandler errorHandler() {

		List<Class<? extends RuntimeException>> exceptionsToBeIgnored = List.of(IllegalArgumentException.class);

		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);
		DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(fixedBackOff);

		exceptionsToBeIgnored.forEach(defaultErrorHandler::addNotRetryableExceptions);

		defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempts) -> {
			log.info("Failed record in retry listener, exception {}, deliveryAttempts {} ", ex.getMessage(),
					deliveryAttempts);
		});
		return defaultErrorHandler;
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory,
			ObjectProvider<ContainerCustomizer<Object, Object, ConcurrentMessageListenerContainer<Object, Object>>> kafkaContainerCustomizer) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory
				.getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties())));
		factory.setCommonErrorHandler(errorHandler());
		return factory;
	}

}
