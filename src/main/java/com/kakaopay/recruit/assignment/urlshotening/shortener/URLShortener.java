package com.kakaopay.recruit.assignment.urlshotening.shortener;

public interface URLShortener<T> {
	int MAX_URL_SIZE_REQUESTED_BY_RECRUIT_TEAM = 8;
	String createShorteningURL(T url, int maxSize);
	default String createShorteningURL(T url) {
		return createShorteningURL(url, MAX_URL_SIZE_REQUESTED_BY_RECRUIT_TEAM);
	}
}