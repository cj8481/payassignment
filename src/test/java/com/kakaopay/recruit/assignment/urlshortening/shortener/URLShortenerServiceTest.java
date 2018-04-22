package com.kakaopay.recruit.assignment.urlshortening.shortener;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;

import com.kakaopay.recruit.assignment.urlshortening.url.ReactiveShortURLRepository;
import com.kakaopay.recruit.assignment.urlshortening.sequence.SequenceRepository;
import com.kakaopay.recruit.assignment.urlshortening.url.ShortURL;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class URLShortenerServiceTest {
	@InjectMocks
	private URLShortenerService sut;

	@Mock
	private URLShortener<Long> base62SplitShortener;
	@Mock
	private ReactiveShortURLRepository repository;
	@Mock
	private SequenceRepository sequenceRepository;

	private String urlHostPath = "test";

	@Before
	public void before() throws IllegalAccessException {
		FieldUtils.writeDeclaredField(sut, "urlHostPath", urlHostPath, true);
	}

	/**
	 * findById 를 정상적으로 호출하는지, 거기에서 나온 mono의 값을 제대로 방출하는지 테스트한다
	 */
	@Test
	public void getOriginalUrl_test() {
		given(repository.findById(any(Publisher.class))).willReturn(Mono.just(ShortURL.builder().originalUrl("originalUrl").build()));

		assertThat(sut.getOriginalUrl("url").block()).isEqualTo("originalUrl");

		verify(repository).findById(any(Mono.class));
	}

	/**
	 * getShortUrlDecodedId 를 통해서 base62SplitShortener.decode 가 제대로 호출 되는지
	 * 거기서 리턴된 값이 정상적으로 방출 되는지 테스트한다
	 */
	@Test
	public void getShortUrlDecodedId_test() {
		given(base62SplitShortener.decode("url")).willReturn(1L);
		assertThat(sut.getShortUrlDecodedId("url").block()).isEqualTo(1L);

		verify(base62SplitShortener).decode("url");
	}

	@Test
	public void getOrCreateShortUrl_값이_존재할때는_그대로_리턴한다() {
		ShortURL url = ShortURL.builder().id(1234L).build();
		given(repository.findByOriginalUrl("url")).willReturn(Mono.just(url));

		StepVerifier.create(sut.getOrCreateShortUrl("url"))
			.expectNext(url)
			.expectComplete();

		verify(repository).findByOriginalUrl("url");
		verify(sequenceRepository, never()).getNextSequenceId(ReactiveShortURLRepository.SEQ_NAME);
		verify(base62SplitShortener, never()).encode(anyLong());
	}

	@Test
	public void getOrCreateShortUrl_값이_없을때는_새로_생성하여_리턴한다() {
		String url = "url";
		given(repository.findByOriginalUrl(url)).willReturn(Mono.empty());
		long inputId = 1234L;
		given(sequenceRepository.getNextSequenceId(ReactiveShortURLRepository.SEQ_NAME)).willReturn(Mono.just(inputId));
		given(base62SplitShortener.encode(inputId)).willReturn("testUrl");

		ShortURL savedUrl = ShortURL.builder().id(5555L).build();
		ShortURL inputUrl = ShortURL.builder().id(inputId).build();
		given(repository.save(any(ShortURL.class))).willReturn(Mono.just(savedUrl));

		ArgumentCaptor<ShortURL> captor = ArgumentCaptor.forClass(ShortURL.class);

		assertThat(sut.getOrCreateShortUrl(url).block()).isEqualTo(savedUrl);

		verify(sequenceRepository).getNextSequenceId(ReactiveShortURLRepository.SEQ_NAME);
		verify(repository).save(captor.capture());
		verify(base62SplitShortener).encode(inputId);

		assertThat(captor.getValue()).isEqualTo(inputUrl);
	}

}