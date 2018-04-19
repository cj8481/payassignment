package com.kakaopay.recruit.assignment.urlshotening.shortener;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

/**
 * base 62 인코딩된 문자열을 8자리 기준으로 잘라내는 형태의 shortener 이다.
 * 실제 8자리까지 제한사항이 있고 그것을 실현하기는 중복 없이는 매우 어렵다.
 * 또 다른 해결 방식으로는 MongoDB의 ObjectId 와 같은 생성 방식같은
 * bitwise 연산을 통해 bit 의 범위를 나누어 시간/머신ID/프로세스ID/카운터 와 같은 방식으로
 * 최대한 회피할 수 있으나 그것 또한 근본적인 중복 방지 해결책은 아니다.
 * 그러므로 가장 최대한 마지막 자리는 8자리를 유지하면서 URL 을 추가로 생성할 수 있는
 * 아래 클래스 방식으로 작성하였다
 */
@AllArgsConstructor
@Component
public class Base62SplitShortener implements URLShortener<Long> {
	private Base62URLShortener base62URLShortener;

	@Override
	public String encode(Long url) {
		final int limitSize = URLShortener.MAX_URL_SIZE_REQUESTED_BY_RECRUIT_TEAM;
		String encodeString = base62URLShortener.encode(url);
		if (encodeString.length() <= limitSize) {
			return "/" + encodeString;
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

		return result
			.insert(0, "/")
			.substring(0, result.lastIndexOf("/"));
	}

	@Override
	public Long decode(String value) {
		return base62URLShortener.decode(value.replaceAll("/", ""));
	}

}
