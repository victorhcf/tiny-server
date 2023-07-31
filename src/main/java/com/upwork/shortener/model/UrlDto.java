package com.upwork.shortener.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * DTO for URL model
 */
@Getter @Setter @NoArgsConstructor @ToString
public class UrlDto {
	
	private String originalUrl;
	private String shorturl;
	private LocalDateTime createdDate;
	private LocalDateTime expirationDate; //optional
	
	public UrlDto(String originalUrl, String shorturl, LocalDateTime createdDate, LocalDateTime expirationDate) {
		super();
		this.originalUrl = originalUrl;
		this.shorturl = shorturl;
		this.createdDate = createdDate;
		this.expirationDate = expirationDate;
	}
	
	public UrlDto(String originalUrl, String shorturl, LocalDateTime expirationDate) {
		super();
		this.originalUrl = originalUrl;
		this.shorturl = shorturl;
		this.expirationDate = expirationDate;
	}
	
	public UrlDto(Url url) {
		this( url.getOriginalUrl(), url.getShortUrl(), url.getCreatedDate(), url.getExpirationDate() );
	}

}
