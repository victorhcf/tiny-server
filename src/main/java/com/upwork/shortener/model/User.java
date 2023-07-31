package com.upwork.shortener.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class User {

	private long id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdDate;
	private LocalDateTime lastLogin;
	
	public User(long id, String name, String email, String password, LocalDateTime createdDate,
			LocalDateTime lastLogin) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdDate = createdDate;
		this.lastLogin = lastLogin;
	}
}
