package com.kakaopay.recruit.assignment.urlshotening.shortener;

import java.math.BigInteger;
import java.util.stream.LongStream;

import org.junit.Test;

public class Base62URLShortenerTest {
	private Base62URLShortener sut = new Base62URLShortener();

	@Test
	public void test() {

		String unsignedString = Long.toUnsignedString(-1L);

		LongStream.range(Long.MIN_VALUE, Long.MAX_VALUE)
			.parallel()
			.mapToObj(url -> sut.createShorteningURL(url))
			.filter(s -> s.length() > 8)
			.forEach(u -> {
				System.out.println(u);
			});
	}
}