package com.kakaopay.recruit.assignment.urlshortening.url;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveShortURLRepository extends ReactiveCrudRepository<ShortURL, Long> {
	String SEQ_NAME = "shorturl";
	Mono<ShortURL> findByOriginalUrl(String url);
}
