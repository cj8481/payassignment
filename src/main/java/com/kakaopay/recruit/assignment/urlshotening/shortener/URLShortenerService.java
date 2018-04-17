package com.kakaopay.recruit.assignment.urlshotening.shortener;

import static reactor.core.publisher.Mono.*;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kakaopay.recruit.assignment.urlshotening.url.ReactiveShortURLRepository;
import com.kakaopay.recruit.assignment.urlshotening.url.SequenceRepository;
import com.kakaopay.recruit.assignment.urlshotening.url.ShortURL;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class URLShortenerService {
	private URLShortener<Long> base62SplitShortener;
	private ReactiveShortURLRepository repository;
	private SequenceRepository sequenceRepository;
	private String urlHostPath;

	public URLShortenerService(
		URLShortener<Long> base62SplitShortener,
		ReactiveShortURLRepository repository,
		SequenceRepository sequenceRepository,
		@Value("${short.url.host}/") String urlHostPath) {
		this.base62SplitShortener = base62SplitShortener;
		this.repository = repository;
		this.sequenceRepository = sequenceRepository;
		this.urlHostPath = urlHostPath;
	}

	@PostConstruct
	public void initSequence() {
		sequenceRepository.initSequence(ReactiveShortURLRepository.SEQ_NAME, -1);
	}

	public Mono<ShortURL> getOrCreateShortUrl(String originalUrl) {
		return repository.findByOriginalUrl(originalUrl)
			.subscribeOn(Schedulers.immediate())
			.switchIfEmpty(createShortUrl(originalUrl));
	}

	public Mono<String> getOriginalUrl(String shortUrl) {
		return repository.findByShortUrl(urlHostPath + shortUrl)
			.map(ShortURL::getOriginalUrl);
	}

	private Mono<ShortURL> createShortUrl(String url) {
		Date createdAt = new Date();
		return fromCallable(() -> sequenceRepository.getNextSequenceId(ReactiveShortURLRepository.SEQ_NAME))
			.publishOn(Schedulers.elastic())
			.publishOn(Schedulers.parallel())
			.map(id -> ShortURL.builder()
					.id(id)
					.originalUrl(url)
					.shortUrl(urlHostPath + base62SplitShortener.encode(id))
					.createdAt(createdAt)
					.build()
				)
			.publishOn(Schedulers.elastic())
			.flatMap(repository::save)
			;
	}
}
