package com.techgen.kafka.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Book {

	@Id
	@Column(name = "book_id")
	private Integer bookId;

	@Column(name = "book_name")
	private String bookName;

	@Column(name = "book_author")
	private String bookAuthor;

	@OneToOne
	@JoinColumn(name = "library_event_id")
	private LibraryEvent libraryEvent;

}
