package com.techgen.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.techgen.kafka.service.LibraryEventService;
import com.techgen.model.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsConsumer {

	@Value("${spring.kafka.topic}")
	private String topic;

	@Autowired
	private LibraryEventService libraryEventService;

	// Both below methods are having the same logic , commented method has parameter
	// of type
	// ConsumerRecord which is useful in-case if we want to get metadata about key,
	// topic, offset, partition

//	@KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
//	public void consumeLibraryEvents(ConsumerRecord<Integer, LibraryEvent> consumerRecord) {
//		log.info("consuming event {} for key {} from topic {} from partition {} with offset {}", consumerRecord.value(),
//				consumerRecord.key(), topic, consumerRecord.partition(), consumerRecord.offset());
//		LibraryEvent libraryEvent = new LibraryEvent();
//		libraryEvent.setLibraryEventId(consumerRecord.value().getLibraryEventId());
//		libraryEvent.setLibraryEventType(consumerRecord.value().getLibraryEventType());
//		libraryEvent.setBook(consumerRecord.value().getBook());
//		libraryEventService.processRecordsToDB(libraryEvent);
//	}

	@KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeLibraryEvents(LibraryEvent libraryEvent) {
		log.info("consuming event {} from topic {}", libraryEvent, topic);
		log.info("");
		libraryEventService.processRecordsToDB(libraryEvent);
	}

}
