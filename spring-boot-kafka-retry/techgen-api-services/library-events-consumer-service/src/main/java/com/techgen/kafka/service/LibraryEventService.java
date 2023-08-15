package com.techgen.kafka.service;

import java.util.Optional;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

import com.techgen.kafka.entity.Book;
import com.techgen.kafka.repository.LibraryEventRepository;
import com.techgen.model.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryEventService {

	private final LibraryEventRepository libraryEventRepository;

	public LibraryEventService(LibraryEventRepository libraryEventRepository) {
		super();
		this.libraryEventRepository = libraryEventRepository;
	}

	public void processRecordsToDB(LibraryEvent libraryEventDto) {

		switch (libraryEventDto.getLibraryEventType()) {
		case NEW: {
			processLibraryEvent(libraryEventDto);
			log.info("record inserted successfully into DB");
			break;
		}
		case UPDATE: {
			validateLibraryEvent(libraryEventDto);
			processLibraryEvent(libraryEventDto);
			log.info("record updated successfully into DB");
			break;
		}
		default:
			log.info("invalid library event type");
		}
	}

	private void validateLibraryEvent(LibraryEvent libraryEventDto) {
		if (libraryEventDto.getLibraryEventId() == null) {
			throw new IllegalArgumentException("Library eventId is missing");
		}
		if (libraryEventDto.getLibraryEventId() != null) {
			throw new RecoverableDataAccessException("N/w is down keep trying");
		}
		Optional<com.techgen.kafka.entity.LibraryEvent> libraryEventOptional = libraryEventRepository
				.findById(libraryEventDto.getLibraryEventId());
		if (!libraryEventOptional.isPresent()) {
			throw new IllegalArgumentException("Invalid Library eventId");
		}
	}

	private com.techgen.kafka.entity.LibraryEvent processLibraryEvent(LibraryEvent libraryEventDto) {
		com.techgen.kafka.entity.LibraryEvent libraryEvent = new com.techgen.kafka.entity.LibraryEvent();
		libraryEvent.setLibraryEventId(libraryEventDto.getLibraryEventId());
		libraryEvent.setLibraryEventType(libraryEventDto.getLibraryEventType());
		Book book = new Book();
		book.setBookId(libraryEventDto.getBook().getBookId());
		book.setBookAuthor(libraryEventDto.getBook().getBookAuthor());
		book.setBookName(libraryEventDto.getBook().getBookName());
		book.setLibraryEvent(libraryEvent);
		libraryEvent.setBook(book);
		libraryEventRepository.save(libraryEvent);
		return libraryEvent;
	}

}
