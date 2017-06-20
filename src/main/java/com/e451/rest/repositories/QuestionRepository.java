package com.e451.rest.repositories;

import com.e451.rest.domains.question.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by e384873 on 6/9/2017.
 */
@Repository
public interface QuestionRepository extends MongoRepository<Question, String>{

}
