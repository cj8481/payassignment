package com.kakaopay.recruit.assignment.urlshotening.url;

public interface SequenceRepository {
	void initSequence(String key, long initialSequence);
	long getNextSequenceId(String key);
}
