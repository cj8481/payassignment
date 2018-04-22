package com.kakaopay.recruit.assignment.urlshortening.shortener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class URLShortenerControllerTest {
	@Autowired
	private WebTestClient client;

	private String originalUrl = "http://www.naver.com";

	private String expectShortUrl = "http://localhost:8080/a";

	@Test
	public void test_create_url_first() {
		client.post()
			.uri("/url")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.syncBody("url=" + originalUrl)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo("0")
			.jsonPath("$.shortUrl").isEqualTo(expectShortUrl)
			.jsonPath("$.originalUrl").isEqualTo(originalUrl)
		;
	}

	@Test
	public void test_create_url_empty_url_badRequest_test() {
		client.post()
			.uri("/url")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.exchange()
			.expectStatus().isBadRequest()
		;
	}

	/**
	 * 테스트를 두번 하여 같은 id 값이 리턴되는지 체크해본다
	 */
	@Test
	public void same_url_return_same_result_first() {
		test_create_url_first();
		test_create_url_first();
	}

	@Test
	public void test_not_found_url() {
		client.get()
			.uri("/aa")
			.exchange()
			.expectStatus().isNotFound()
			.expectBody();
	}

	@Test
	public void test_created_url_redirect_test() {
		test_create_url_first();

		client.get()
			.uri("/a")
			.exchange()
			.expectStatus().is3xxRedirection()
			.expectHeader()
			.valueEquals("location", originalUrl)
			;
	}
}