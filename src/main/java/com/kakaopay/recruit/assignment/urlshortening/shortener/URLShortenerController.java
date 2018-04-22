package com.kakaopay.recruit.assignment.urlshortening.shortener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kakaopay.recruit.assignment.urlshortening.url.ShortURL;

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
		return service.getOrCreateShortUrl(url).log();
	}

	@GetMapping("/createUrl")
	public String index() {
		return "createUrl";
	}

	@GetMapping("/**")
	public Mono<ResponseEntity<String>> redirectToOriginalUrl(HttpServletRequest request) {
		return service.getOriginalUrl(request.getRequestURI())
			.map(u -> ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).header("location", u).body(""))
			.defaultIfEmpty(NOT_FOUND)
			.log()
			;
	}
}
