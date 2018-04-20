package com.kakaopay.recruit.assignment.urlshotening.url;

import static org.assertj.core.api.Java6Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SequenceRepositoryTest {
	@Autowired
	private SequenceRepository repository;

	@Test
	public void init_getSequence_test() {
		repository.initSequence("seq", 0);
		assertThat(repository.getNextSequenceId("seq").block()).isEqualTo(1L);
	}
}