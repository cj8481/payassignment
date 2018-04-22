package com.kakaopay.recruit.assignment.urlshotening.sequence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "sequence")
@Getter
@Setter
public class SequenceID {
	@Id
	private String id;
	private long seq;
}
