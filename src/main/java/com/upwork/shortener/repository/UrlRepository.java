package com.upwork.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upwork.shortener.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>{

	public Url findByShortUrl(String shortUrl);
}
