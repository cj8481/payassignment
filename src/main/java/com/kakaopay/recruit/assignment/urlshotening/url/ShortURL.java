package com.kakaopay.recruit.assignment.urlshotening.url;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Document(collection = "shortUrl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ShortURL {
	@Id
	private Long id;

	private String shortUrl;

	@Indexed(name = "original_url_index", unique = true)
	private String originalUrl;

	private Date createdAt;
}
