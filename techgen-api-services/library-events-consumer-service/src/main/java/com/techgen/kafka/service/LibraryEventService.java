package com.techgen.kafka.service;

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
		log.info("record processed successfully to DB");
	}

}
