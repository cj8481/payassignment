package com.kakaopay.recruit.assignment.urlshortening.sequence;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@AllArgsConstructor
public class SequenceRepositoryImpl implements SequenceRepository {
	private ReactiveMongoOperations mongoOperation;

	@Override
	public void initSequence(String key, long initialSequence) {
		Query query = new Query(Criteria.where("_id").is(key).and("seq").ne(initialSequence));

		Update update = new Update();
		update.setOnInsert("seq", initialSequence);
		mongoOperation.upsert(query, update, SequenceID.class).block();
	}

	@Override
	public Mono<Long> getNextSequenceId(String key) {
		Query query = new Query(Criteria.where("_id").is(key));
		Update update = new Update();
		update.inc("seq", 1);

		FindAndModifyOptions options = new FindAndModifyOptions()
			.returnNew(true);

		return mongoOperation.findAndModify(query, update, options, SequenceID.class)
			.map(SequenceID::getSeq);

	}
}
