package com.upwork.shortener.service;

import com.upwork.shortener.model.Url;
import com.upwork.shortener.model.UrlDto;

public interface ShortenerServiceI {

	public Url generateShorter(UrlDto urlDto);
	public Url saveShort(Url url);
	public UrlDto getShorter(String code);
	public void deleteUrl(Url url);
	
}
