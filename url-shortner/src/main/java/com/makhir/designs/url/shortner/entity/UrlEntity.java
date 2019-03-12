package com.makhir.designs.url.shortner.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@RedisHash(value="url-entities", timeToLive=60*60*24*30) //1 month
public class UrlEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	@JsonProperty(required=true, value="actual-url")
	@Indexed
	private String actualUrl;
	@JsonProperty(value="create-date")
	@Indexed
	private Date createDate;
}
