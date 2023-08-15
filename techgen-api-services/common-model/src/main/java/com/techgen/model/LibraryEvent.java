package com.techgen.model;

import com.techgen.enums.LibraryEventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibraryEvent {

	private Integer libraryEventId;

	private LibraryEventType libraryEventType;

	private Book book;

}
