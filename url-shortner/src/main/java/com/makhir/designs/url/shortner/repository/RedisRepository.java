package com.makhir.designs.url.shortner.repository;

import org.springframework.data.repository.CrudRepository;

import com.makhir.designs.url.shortner.entity.UrlEntity;

public interface RedisRepository extends CrudRepository<UrlEntity, String>{

}
