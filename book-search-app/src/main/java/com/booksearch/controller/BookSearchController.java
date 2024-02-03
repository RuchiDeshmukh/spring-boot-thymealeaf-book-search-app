package com.booksearch.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.booksearch.config.BookSearchConfig;
import com.booksearch.model.Book;
import com.booksearch.model.BookDetails;
import com.booksearch.model.BookSearchResult;
import com.booksearch.model.Description;

@Controller
public class BookSearchController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BookSearchConfig bookSearchConfig;

	@GetMapping("/")
	public String getHomePage() {
		return "index.html";
	}

	@GetMapping("/books")
	public String getBooks(@RequestParam String query, @RequestParam(required = false) Integer pageNo, Model model) {
		model.addAttribute("query", query);

		if (Objects.isNull(pageNo))
			pageNo = 1;
		int limit = 6;
		
		Map<String, Object> map = new HashMap<>();
		map.put("query", query.replace("\\s", "-"));
		map.put("pageNo", pageNo);
		map.put("limit", limit);

		ResponseEntity<BookSearchResult> bookSearchResult = restTemplate.getForEntity(
				bookSearchConfig.getUrl() + "q={query}&page={pageNo}&limit={limit}", BookSearchResult.class, map);

		List<Book> books = bookSearchResult.getBody().getDocs();
		for (Book book : books) {
			String coverUrl = bookSearchConfig.getNoImage();
			if (Objects.nonNull(book.getCover_i())) {
				coverUrl = bookSearchConfig.getCoverBaseUrl() + book.getCover_i() + bookSearchConfig.getCoverSize();
			}
			book.setCoverUrl(coverUrl);
			if(Objects.nonNull(book.getAuthor_name())) {
				book.setAuthor(book.getAuthor_name().get(0));
			}
			
		}
		model.addAttribute("books", books);

		return "index.html";
	}

	@GetMapping("/viewBook")
	public String viewBook(@RequestParam String key, Model model) {

		// @RequestParam String key;
		StringBuilder url = new StringBuilder();
		url.append(bookSearchConfig.getBookDetailsUrl());
		url.append(key.replace("/works/", ""));
		url.append(bookSearchConfig.getBookDetailsUrlSuffix());
		ResponseEntity<BookDetails> response = restTemplate.getForEntity(url.toString(), BookDetails.class);

		BookDetails bookDetails = response.getBody();
		String description = "Book description not found!";
		if (Objects.nonNull(bookDetails.getDescription())) {
			description = bookDetails.getDescription().getValue();
			int index = description.indexOf("--");
			if (index != -1)
				description = description.substring(0, index);
			index = description.indexOf("([source]");
			if (index != -1)
				description = description.substring(0, index);
			bookDetails.getDescription().setValue(description);
			List<String> subjects = bookDetails.getSubjects().stream().limit(5).collect(Collectors.toList());
			bookDetails.setSubjects(subjects);

		} else {
			Description desc = new Description();
			desc.setValue(description);
			bookDetails.setDescription(desc);
			bookDetails.setSubjects(Arrays.asList("Not found!"));
		}

		String coverUrl = bookSearchConfig.getNoImageBook();
		if (Objects.nonNull(bookDetails.getCovers())) {
			String coverId = bookDetails.getCovers().get(0);
			coverUrl = bookSearchConfig.getCoverBaseUrl() + coverId + bookSearchConfig.getCoverSize();
		}

		model.addAttribute("bookDetails", bookDetails);
		model.addAttribute("coverUrl", coverUrl);
		return "book.html";
	}

}
