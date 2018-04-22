package com.kakaopay.recruit.assignment.urlshortening.shortener;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

public class Base62SplitShortenerTest {
	private final Base62URLShortener base62URLShortener = new Base62URLShortener();
	private Base62SplitShortener sut = new Base62SplitShortener(base62URLShortener);

	@Test
	public void encode() {
		assertThat(sut.encode(base62URLShortener.decode("aaaaaaaab"))).isEqualTo("/a/aaaaaaab");
	}

	@Test
	public void encode_long_length() {
		assertThat(sut.encode(Long.MIN_VALUE)).isEqualTo("/iWi/fIaXiv9k");
	}

	@Test
	public void decode_test() {
		assertThat(sut.decode("/a/aaaaaaab")).isEqualTo(base62URLShortener.decode("aaaaaaaab"));
	}

	@Test
	public void decode_long_length_test() {
		assertThat(sut.decode("/aaaaaaaa/aaaaaaaa/aaaaaaab")).isEqualTo(base62URLShortener.decode("aaaaaaaaaaaaaaaaaaaaaaab"));
	}

}