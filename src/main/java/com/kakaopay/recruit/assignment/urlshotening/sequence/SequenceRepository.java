package com.kakaopay.recruit.assignment.urlshotening.sequence;

import reactor.core.publisher.Mono;

public interface SequenceRepository {
	void initSequence(String key, long initialSequence);
	Mono<Long> getNextSequenceId(String key);
}
