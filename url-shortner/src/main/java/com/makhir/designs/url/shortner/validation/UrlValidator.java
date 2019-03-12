package com.makhir.designs.url.shortner.validation;

import java.net.URL;

import org.springframework.util.PatternMatchUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlValidator {
	public static final String EMAIL_REGEX_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
	/**
	 * This method returns true if provided url is valid against the url regex pattern.
	 * */
	public static boolean validateUrl(String url){
		log.debug("Validating received url against patter.");
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * This method returns true if provided email is valid against the email regex pattern.
	 * */
	public static boolean validateEmail(String email){
		log.debug("Validating received email against patter.");
		return PatternMatchUtils.simpleMatch(EMAIL_REGEX_PATTERN, email);
	}
}
