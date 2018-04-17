package com.kakaopay.recruit.assignment.urlshotening.shortener;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.stream.LongStream;

import org.junit.Test;
import org.springframework.data.util.Pair;

public class Base62URLShortenerTest {
	private Base62URLShortener sut = new Base62URLShortener();

	/**
	 * encode / decode 를 병행해서 원래의 값을 복원할 수 있는지에 대한 테스트
	 * 모든 자릿수를 테스트하면 너무 오래걸리기 때문에 2자리 숫자 + 1개 정도까지 테스트 하여 전체적인 검증을 보장한다.
	 */
	@Test
	public void test_encode_decode() {
		LongStream.rangeClosed(0, sut.BASE62LENGTH * sut.BASE62LENGTH + 1)
			.parallel()
			.forEach(i -> {
				String encode = sut.encode(i);
				assertThat(sut.decode(encode)).isEqualTo(i);
			});
	}

	/**
	 * 마이너스 테스트로 마이너스 값도 정상적으로 동작하는지 테스트 한다.
	 * 내부적으로 unsigned 변환을 하고 있기 때문에 마이너스도 정상 처리 되어야 한다
	 */
	@Test
	public void minus_test() {
		assertThat(sut.decode(sut.encode(-1L))).isEqualTo(-1L);
	}
}