package com.booksearch.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {

	private Long cover_i;
	private String title;
	private List<String> author_name;
	private String key;
	private List<String> author_key;
	private String coverUrl;
	private String author;
}
