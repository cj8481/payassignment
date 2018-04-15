package com.kakaopay.recruit.assignment.urlshotening.url;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SequenceRepositoryImpl implements SequenceRepository {
	private MongoOperations mongoOperation;

	@Override
	public void initSequence(String key, long initialSequence) {
		SequenceID sequenceId = new SequenceID();
		sequenceId.setId(key);
		sequenceId.setSeq(initialSequence);
		mongoOperation.insert(sequenceId);
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
