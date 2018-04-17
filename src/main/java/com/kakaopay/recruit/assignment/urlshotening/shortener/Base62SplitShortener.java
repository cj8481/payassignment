package com.kakaopay.recruit.assignment.urlshotening.shortener;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class Base62SplitShortener implements URLShortener<Long> {
	private Base62URLShortener base62URLShortener;
	private final int limitSize = URLShortener.MAX_URL_SIZE_REQUESTED_BY_RECRUIT_TEAM;

	@Override
	public String encode(Long url) {
		String encodeString = base62URLShortener.encode(url);
		if (encodeString.length() <= limitSize) {
			return encodeString;
		}

		StringBuilder result = new StringBuilder();
		int length = encodeString.length();
		int end = length / limitSize;
		int beginIndex = length - limitSize;
		int endIndex = beginIndex + limitSize;
		for (int i = 0; i <= end; i++) {
			if (beginIndex < 0) {
				beginIndex = 0;
				endIndex = length % limitSize;
			}

			result
				.insert(0, "/")
				.insert(0, encodeString.substring(beginIndex, endIndex));
			beginIndex -= limitSize;
			endIndex -= limitSize;
		}

		return result.substring(0, result.lastIndexOf("/"));
	}

	@Override
	public Long decode(String value) {
		return base62URLShortener.decode(value.replaceAll("/", ""));
	}
}
