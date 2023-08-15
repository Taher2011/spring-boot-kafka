package com.techgen.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techgen.model.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsProducer {

	@Value("${spring.kafka.topic}")
	private String topic;

	private final KafkaTemplate<Integer, LibraryEvent> kafkaTemplate;

	public LibraryEventsProducer(KafkaTemplate<Integer, LibraryEvent> kafkaTemplate) {
		super();
		this.kafkaTemplate = kafkaTemplate;
	}

	public void produceLibraryEvents(LibraryEvent libraryEvent) throws JsonProcessingException {
		log.info("started producing libraryevent to topic {},  and value {} ", topic, libraryEvent);
		kafkaTemplate.send(topic, libraryEvent);
		log.info("completed producing libraryevent to topic {},  and value {} ", topic, libraryEvent);
	}

}
