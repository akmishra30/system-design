package com.makhir.designs.url.shortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends Exception {
	private String url;
	private String message;
	
	public UrlNotFoundException(String message, String url){
		this.message = message;
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	@Override
	public String getMessage(){
		return message.concat(url);
	}
}
