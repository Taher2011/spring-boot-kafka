package com.techgen.kafka.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {

	private Integer bookId;

	private String bookName;

	private String bookAuthor;

}
