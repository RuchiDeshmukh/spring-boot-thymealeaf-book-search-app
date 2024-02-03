package com.booksearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="book.search")
@Data
public class BookSearchConfig {

	private String url;
	private String coverBaseUrl;
	private String coverSize;
	private String bookDetailsUrl;
	private String bookDetailsUrlSuffix;
	private String noImage;
	private String noImageBook;

}
