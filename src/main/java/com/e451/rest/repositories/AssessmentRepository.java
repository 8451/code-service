package com.e451.rest.repositories;

import com.e451.rest.domains.assessment.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by j747951 on 6/15/2017.
 */
@Repository
public interface AssessmentRepository extends MongoRepository<Assessment, String> {
    Assessment findByInterviewGuid(String guid);
}
