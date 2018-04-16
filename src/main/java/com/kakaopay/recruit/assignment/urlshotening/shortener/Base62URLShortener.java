package com.kakaopay.recruit.assignment.urlshotening.shortener;

import org.springframework.stereotype.Component;

@Component
public class Base62URLShortener implements URLShortener<Long> {
	public static final String base62Table =
		"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final int BASE62LENGTH = base62Table.length();

	@Override
	public String createShorteningURL(Long url) {
		if (url == 0L) {
			return String.valueOf(base62Table.charAt(0));
		}
		StringBuilder result = new StringBuilder();
		while (url > 0) {
			result.append(base62Table.charAt((int) (Long.remainderUnsigned(url, BASE62LENGTH))));
			url = Long.divideUnsigned(url, BASE62LENGTH);
		}
		return result.toString();
	}
}
