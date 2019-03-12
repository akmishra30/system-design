package com.makhir.designs.url.shortner.service;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.makhir.designs.url.shortner.entity.UrlEntity;
import com.makhir.designs.url.shortner.entity.UrlResponse;
import com.makhir.designs.url.shortner.exception.UrlNotFoundException;
import com.makhir.designs.url.shortner.repository.RedisRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlShortnerService {
	
	private RandomStringGenerator generator = null;
	
	@Value("${com.makhir.url-key.length}")
	private int keyLength;
	
	public UrlShortnerService() {
		generator = new RandomStringGenerator.
					Builder()
					.withinRange('0', 'z')
			        .filteredBy(LETTERS, DIGITS).
			        build();
	}
	
	@Autowired
	private RedisRepository redisRepository;
	
	public UrlResponse getShortedUrl(UrlEntity urlEntity) throws Exception{
		UrlResponse urlResponse = null;
		String uniqueKey = createUniqueId();
		urlEntity.setId(uniqueKey);
		urlEntity.setCreateDate(new Date());
		log.debug("## UrlEntity to be saved in DB :: {}", urlEntity);
		UrlEntity saved = redisRepository.save(urlEntity); 
		
		if(saved != null) {
			urlResponse = new UrlResponse();
			urlResponse.setShortedUrl(uniqueKey);
			urlResponse.setCreatedDate(new Date());
			log.info("## Generated shorted url: {}", urlResponse);
			return urlResponse;
		} else {
			throw new Exception("Unable to save entity,");
		}
	}
	
	public UrlEntity getRedirectionUrl(String id) throws UrlNotFoundException{
		log.info("## Getting redirected url for id: {}", id);
		Optional<UrlEntity> entity = redisRepository.findById(id);
		
		log.info("## Url is present with given key: {}", entity.isPresent());
		
		if(!entity.isPresent())
			throw new UrlNotFoundException("The following url is not found in our url-shortner system. Please check and try again.", "");
		
		return entity.get();
	}
	
	public List<UrlEntity> fetchAllUrls() throws UrlNotFoundException{
		log.info("## Getting all the urls saved in cache.");
		
		List<UrlEntity> entities = new ArrayList<UrlEntity>();
		
		redisRepository.findAll().forEach(entity ->{
			entities.add(entity);
		});
		
		log.info("## Total url fetched. {}", entities.size());
		
		return entities;
	}
	
	/**
	 * This method generates 6 char length unique random alpha-numeric string key.
	 * 
	 * */
	private String createUniqueId(){
		return generator.generate(keyLength);
	}
}
