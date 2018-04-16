package com.kakaopay.recruit.assignment.urlshotening.shortener;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kakaopay.recruit.assignment.urlshotening.url.ShortURL;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Controller
public class URLShortenerController {
	private final ResponseEntity<String> NOT_FOUND = new ResponseEntity<>(HttpStatus.NOT_FOUND);
	private URLShortenerService service;

	@PostMapping("/url")
	@ResponseBody
	public Mono<ShortURL> createOrGetShortUrl(@RequestParam String url) {
		return service.getOrCreateShortUrl(url);
	}

	@GetMapping("/{shortUrl}")
	public Mono<ResponseEntity<String>> redirectToOriginalUrl(@PathVariable String shortUrl) {
		return service.getOriginalUrl(shortUrl)
			.map(u -> "redirect:" + u)
			.map(u -> ResponseEntity.ok().body(u))
			.defaultIfEmpty(NOT_FOUND)
			.log()
			;
	}
}
