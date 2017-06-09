package com.e451.rest.repositories;

import com.e451.rest.domains.question.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by e384873 on 6/9/2017.
 */
public interface QuestionRepository extends MongoRepository<Question, String>{

}
