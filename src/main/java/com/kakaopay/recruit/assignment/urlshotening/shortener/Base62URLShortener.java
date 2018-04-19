package com.kakaopay.recruit.assignment.urlshotening.shortener;

import org.springframework.stereotype.Component;

/**
 * Base 62 기반의 문자열 변환기 입니다.
 * 기존의 Base 62 알고리즘과 차이가 있다면 Long 을 활용하되 Unsigned 형태로 최대한 많은 숫자를 포함할 수 있습니다.
 */
@Component
public class Base62URLShortener implements URLShortener<Long> {
	private static final String BASE_62_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final char[] BASE_62_TABLE = BASE_62_STRING.toCharArray();
	public static final int BASE62LENGTH = BASE_62_TABLE.length;

	@Override
	public String encode(Long url) {

		StringBuilder result = new StringBuilder();
		 do {
			result.append(BASE_62_TABLE[(int) Long.remainderUnsigned(url, BASE62LENGTH)]);
			url = Long.divideUnsigned(url, BASE62LENGTH);
		} while (Long.toUnsignedString(url).compareTo("0") > 0);

		return result.toString();
	}

	@Override
	public Long decode(String value) {
		long result = 0;
		long power = 1;
		for (int i = 0; i < value.length(); i++) {
			int digit = BASE_62_STRING.indexOf(value.charAt(i));
			result += digit * power;
			power *= BASE62LENGTH;
		}
		return result;
	}
}
