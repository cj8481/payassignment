package com.kakaopay.recruit.assignment.urlshortening.shortener;

public interface URLShortener<T> {
	int MAX_URL_SIZE_REQUESTED_BY_RECRUIT_TEAM = 8;
	String encode(T url);

	Long decode(String value);
}
