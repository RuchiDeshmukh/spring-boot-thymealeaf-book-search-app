package com.booksearch.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookSearchResult {
	
	private Long numFound;
	private List<Book> docs;

}
