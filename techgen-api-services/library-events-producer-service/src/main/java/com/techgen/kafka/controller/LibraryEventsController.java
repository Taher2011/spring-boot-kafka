package com.techgen.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techgen.kafka.enums.LibraryEventType;
import com.techgen.kafka.model.LibraryEvent;
import com.techgen.kafka.producer.LibraryEventsProducer;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/library")
@Slf4j
public class LibraryEventsController {

	@Autowired
	private final LibraryEventsProducer libraryEventsProducer;

	public LibraryEventsController(LibraryEventsProducer libraryEventsProducer) {
		super();
		this.libraryEventsProducer = libraryEventsProducer;
	}

	@PostMapping("")
	public ResponseEntity<LibraryEvent> createLibraryEvent(@RequestBody LibraryEvent libraryEvent)
			throws JsonProcessingException {
		log.info("started creating library event");
		libraryEventsProducer.produceLibraryEvents(libraryEvent);
		log.info("completed creating library event");
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}

	@PutMapping("")
	public ResponseEntity<?> updateLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
		log.info("started updating library event");
		ResponseEntity<String> event = validateLibraryEvent(libraryEvent);
		if (event != null) {
			return event;
		}
		libraryEventsProducer.produceLibraryEvents(libraryEvent);
		log.info("completed creating library event");
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}

	private ResponseEntity<String> validateLibraryEvent(LibraryEvent libraryEvent) {
		if (libraryEvent.getLibraryEventId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide libraryEventId");
		}
		if (!LibraryEventType.UPDATE.equals(libraryEvent.getLibraryEventType())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only Support UPDATE libraryEventType");
		}
		return null;
	}
}
