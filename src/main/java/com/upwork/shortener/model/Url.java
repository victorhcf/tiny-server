package com.upwork.shortener.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Url {

	@Id 
	@GeneratedValue
	private long id;
	
	@Lob
	@Column(name="original_url")
	private String originalUrl;
	@Column(name="short_url")
	private String shortUrl;
	@Column(name="created_date")
	private LocalDateTime createdDate;
	@Column(name="expiration_date")
	private LocalDateTime expirationDate;
	
	public Url(String originalUrl, String shortUrl, LocalDateTime createdDate, LocalDateTime expirationDate) {
		super();
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrl;
		this.createdDate = createdDate;
		this.expirationDate = expirationDate;
	}
		
	public Url(UrlDto dto) {
		this( dto.getOriginalUrl(), dto.getShorturl(), dto.getCreatedDate(), dto.getExpirationDate()  );
	}
	
	
	
}
