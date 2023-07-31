package com.upwork.shortener.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.upwork.shortener.model.Url;
import com.upwork.shortener.model.UrlDto;
import com.upwork.shortener.repository.UrlRepository;

import io.micrometer.common.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Service to implement all business rules related to URL models
 */
@Component
public class ShortenerService implements ShortenerServiceI {

	private final String REDIS_SERVER = "localhost";
	private final int REDIS_PORT = 6379;
	private final String REDIS_COUNTER_KEY = "counter";
	
	
	@Autowired
	private UrlRepository UrlRepository;
	
	@Override
	public Url generateShorter(UrlDto urlDto) {
		
		if (StringUtils.isNotEmpty( urlDto.getOriginalUrl() )) {
			
			String shortCode = generateShortCode();
			
			Url urlToSave = new Url(urlDto);
			urlToSave.setCreatedDate( LocalDateTime.now() );
			urlToSave.setShortUrl(shortCode);
			urlToSave.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToSave.getCreatedDate()));
			
			Url newUrl = saveShort(urlToSave);
			
			if (newUrl != null)
				return newUrl;
			
		}
		return null;
	}

	/**
	 * This function has the logic to generate the unique code for each url.
	 * We are using a redis server to generate a atomic counter that we use to generate new shortener URLs.
	 */
	private String generateShortCode() {
		try (JedisPool pool = new JedisPool(REDIS_SERVER, REDIS_PORT)) {
			Jedis jedis = pool.getResource(); 
			return Base62.encodeBase62( jedis.incr(REDIS_COUNTER_KEY) );
		}
	}
	
	private LocalDateTime getExpirationDate(LocalDateTime expirationDate, LocalDateTime creationDate){
        if(expirationDate == null)
            return creationDate.plusSeconds(3600);
        
        return expirationDate;
    }
	

	@Override
	public Url saveShort(Url url) {
		return UrlRepository.save(url);
	}

	@Override
	public UrlDto getShorter(String code) {
		
		UrlDto cachedUrl = getUrlFromCache(code);
		if (cachedUrl != null)
			return cachedUrl;
		else {
			Url urlDb = UrlRepository.findByShortUrl(code);
			
			if (urlDb != null) {
				UrlDto urlDto = new UrlDto(urlDb);
				saveUrlToCache( urlDto );
				return urlDto;
			}
		}
		return null;
	}

	
	private UrlDto getUrlFromCache(String code) {
		try (JedisPool pool = new JedisPool(REDIS_SERVER, REDIS_PORT)) {
			Jedis jedis = pool.getResource(); 
			Map<String, String> map = jedis.hgetAll(code);
			
			if ( !map.isEmpty() ) {
				DateTimeFormatter formater = DateTimeFormatter.ISO_DATE_TIME;
				LocalDateTime expirationDate = LocalDateTime.parse( map.get("expirationDate"), formater); 
				return new UrlDto( map.get("originalUrl"), code, expirationDate );
			}
			return null;
		}
	}
	
	private void saveUrlToCache(UrlDto urlDto) {
		try (JedisPool pool = new JedisPool(REDIS_SERVER, REDIS_PORT)) {
			Jedis jedis = pool.getResource();
			
			if (urlDto != null) {
				Map<String, String> map1 = new HashMap<>();
				map1.put("originalUrl", urlDto.getOriginalUrl() );
				DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
				map1.put("expirationDate", urlDto.getExpirationDate().format(formatter));
				jedis.hset(urlDto.getShorturl(), map1);
			}
		}
		
		
	}
	
	@Override
	public void deleteUrl(Url url) {
		UrlRepository.delete(url);		
	}
	
	public List<Url> listAll(){
		return UrlRepository.findAll();
	}


}
