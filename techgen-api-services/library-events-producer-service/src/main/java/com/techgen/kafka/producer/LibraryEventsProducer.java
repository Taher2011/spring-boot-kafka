package com.techgen.kafka.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techgen.model.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsProducer {

	@Value("${spring.kafka.topic}")
	private String topic;

	private final KafkaTemplate<Integer, LibraryEvent> kafkaTemplate;

	private final ObjectMapper objectMapper;

	public LibraryEventsProducer(KafkaTemplate<Integer, LibraryEvent> kafkaTemplate, ObjectMapper objectMapper) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	// Approach 1
	public void produceLibraryEvents(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		log.info("started producing libraryevent to topic {}, with key {} and value {} ", topic, key, libraryEvent);
		CompletableFuture<SendResult<Integer, LibraryEvent>> completableFuture = kafkaTemplate.send(topic, key,
				libraryEvent);
		log.info("completed producing libraryevent to topic {}, with key {} and value {} ", topic, key, libraryEvent);
		completableFuture.whenComplete((sendResult, throwable) -> {
			if (throwable != null) {
				handleFailure(key, value, throwable);
			} else {
				handleSuccess(key, value, sendResult);
			}
		});
	}

	// Approach 2
	public void produceLibraryEventsV2(LibraryEvent libraryEvent)
			throws JsonProcessingException, InterruptedException, ExecutionException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		log.info("started producing libraryevent to topic {}, with key {} and value {} ", topic, key, libraryEvent);
		try {
			SendResult<Integer, LibraryEvent> sendResult = kafkaTemplate.send(topic, key, libraryEvent).get();
			log.info("completed producing libraryevent to topic {}, with key {} and value {} ", topic, key,
					libraryEvent);
			handleSuccess(key, value, sendResult);
		} catch (Exception e) {
			handleFailure(key, value, e);
			Thread.currentThread().interrupt();
		}
	}

	private void handleFailure(Integer key, String value, Throwable throwable) {
		log.error("exception due to ", throwable.getMessage(), throwable);
	}

	private void handleSuccess(Integer key, String value, SendResult<Integer, LibraryEvent> sendResult) {
		log.info("message sent successfully for key {}, value {}, and partition is {} with offset {}", key, value,
				sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
	}

}
