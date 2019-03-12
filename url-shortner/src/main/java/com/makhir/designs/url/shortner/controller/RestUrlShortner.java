package com.makhir.designs.url.shortner.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.makhir.designs.url.shortner.entity.UrlEntity;
import com.makhir.designs.url.shortner.entity.UrlResponse;
import com.makhir.designs.url.shortner.exception.InvalidUrlException;
import com.makhir.designs.url.shortner.exception.UrlNotFoundException;
import com.makhir.designs.url.shortner.service.UrlShortnerService;
import com.makhir.designs.url.shortner.validation.UrlValidator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/")
public class RestUrlShortner {
	
	@Autowired
	private UrlShortnerService urlShortnerService;
	
	private static String HOST;

	@RequestMapping(path="/url/short", method=RequestMethod.POST)
	public ResponseEntity<UrlResponse> createShortUrl(@RequestBody @Valid final UrlEntity urlEntity, HttpServletRequest request) throws Exception{
		log.info("## urlEntity: {}", urlEntity);
		if(!UrlValidator.validateUrl(urlEntity.getActualUrl()))
			throw new InvalidUrlException("The provided URL is incorrect. Please try with correct url. Incorrect url  ", urlEntity.getActualUrl());
		UrlResponse response = urlShortnerService.getShortedUrl(urlEntity);
		response.setShortedUrl(getShortedUrl(request, response.getShortedUrl()));
		log.info("## response: {}", response);
		
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	public RedirectView redirectUrl(@PathVariable String id) throws UrlNotFoundException{
		log.info("## URL to decode id: {}", id);
		
		UrlEntity entity = urlShortnerService.getRedirectionUrl(id);
		log.info("##entity: {}", entity);
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(entity.getActualUrl());
		return redirectView;
	}
	
	@RequestMapping(path="/url/list", method=RequestMethod.GET)
	public ResponseEntity<?> fetchAllTheUrls() throws UrlNotFoundException{
		log.info("## URL to decode id: {}");
		return ResponseEntity.ok(urlShortnerService.fetchAllUrls());
	}
	
	/**
	 * This method generates the given full URL with given host and given key.
	 * */
	private String getShortedUrl(HttpServletRequest request, String key){
		if(HOST == null)
			HOST = request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort();
		return HOST + "/" + key;
	}

}
