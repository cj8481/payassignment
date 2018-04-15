package com.kakaopay.recruit.assignment.urlshotening.url;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class SequenceRepositoryImpl implements SequenceRepository {
	private MongoOperations mongoOperation;

	@Override
	public void initSequence(String key, long initialSequence) {
		Query query = new Query(Criteria.where("_id").is(key).and("seq").ne(initialSequence));

		Update update = new Update();
		update.setOnInsert("seq", initialSequence);
		try {
			mongoOperation.findAndModify(query, update, new FindAndModifyOptions().upsert(true), SequenceID.class);
		} catch (DuplicateKeyException e) {
			log.info("key : {}, seq : {} exists", key, mongoOperation.findOne(new Query(Criteria.where("_id").is(key)), SequenceID.class).getSeq());
		}
	}

	@Override
	public long getNextSequenceId(String key) {
		Query query = new Query(Criteria.where("_id").is(key));
		Update update = new Update();
		update.inc("seq", 1);

		FindAndModifyOptions options = new FindAndModifyOptions()
			.returnNew(true);

		return mongoOperation.findAndModify(query, update, options, SequenceID.class).getSeq();

	}
}
