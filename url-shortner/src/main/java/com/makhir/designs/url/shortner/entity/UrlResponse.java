package com.makhir.designs.url.shortner.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UrlResponse {
	@JsonProperty(value="shorten-url")
	private String shortedUrl;
	@JsonProperty(value="created-date")
	private Date createdDate;
}
