package com.booksearch.model;

import java.util.List;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class BookDetails {
	
	private String title;
	private List<String> covers;
	private List<String> subjects;
	private Description description;

}
