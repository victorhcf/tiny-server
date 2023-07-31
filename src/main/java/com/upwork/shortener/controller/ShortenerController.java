package com.upwork.shortener.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.upwork.shortener.model.ResponseErrorDto;
import com.upwork.shortener.model.Url;
import com.upwork.shortener.model.UrlDto;
import com.upwork.shortener.service.ShortenerService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("http://localhost:8081/")
public class ShortenerController {

	@Autowired
	private ShortenerService urlService;
	
	@PostMapping("/generate")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) throws MalformedURLException{
		
		if ( !isValidUrl( urlDto.getOriginalUrl() ) ) {
			ResponseErrorDto responseDto = new ResponseErrorDto();
	    	responseDto.setError("Please enter a valid URL.");
	    	responseDto.setStatus("400");
	        return new ResponseEntity<ResponseErrorDto>(responseDto, HttpStatus.BAD_REQUEST);
		}
		
		Url urlToReturn = urlService.generateShorter(urlDto);
		
		if (urlToReturn != null) {
			UrlDto dtoResponse = new UrlDto(urlToReturn);
			return new ResponseEntity<UrlDto>(dtoResponse, HttpStatus.OK);
		}
		
		return new ResponseEntity<UrlDto>(HttpStatus.BAD_GATEWAY);		
	}
	
	
	@GetMapping("/list")
	public List<Url> listAll(){
		return urlService.listAll();
	}
	
	@GetMapping("/{shortLink}")
	public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
	
	    if(StringUtils.isEmpty(shortLink))
	    {
	    	ResponseErrorDto responseDto = new ResponseErrorDto();
	    	responseDto.setError("Invalid Url");
	    	responseDto.setStatus("400");
	        return new ResponseEntity<ResponseErrorDto>(responseDto, HttpStatus.NOT_FOUND);
	    }
	    
	    UrlDto urlToRet = urlService.getShorter(shortLink);
	
	    if(urlToRet == null)
	    {
	    	ResponseErrorDto responseDto = new ResponseErrorDto();
	    	responseDto.setError("Url does not exist or it might have expired!");
	    	responseDto.setStatus("400");
	        return new ResponseEntity<ResponseErrorDto>(responseDto, HttpStatus.FOUND);
	    }
	
	    if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
	    {
	        ResponseErrorDto responseDto = new ResponseErrorDto();
	        responseDto.setError("Url Expired. Please try generating a fresh one.");
	        responseDto.setStatus("200");
	        return new ResponseEntity<ResponseErrorDto>(responseDto, HttpStatus.OK);
	    }
	
	    response.sendRedirect( urlToRet.getOriginalUrl( ));
	    return null;
	}
	
	@SuppressWarnings("deprecation")
	private boolean isValidUrl(String url) throws MalformedURLException {
	    try {
	        // it will check only for scheme and not null input 
	        new URL(url);
	        return true;
	    } catch (MalformedURLException e) {
	        return false;
	    }
	}
}
