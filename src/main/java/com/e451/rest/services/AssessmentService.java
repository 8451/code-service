package com.e451.rest.services;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by j747951 on 6/15/2017.
 */
public interface AssessmentService {
    List<Assessment> getAssessments();
    Page<Assessment> getAssessments(Pageable pageable);
    Assessment getAssessmentByGuid(String guid);
    AssessmentState getAssessmentStateByGuid(String guid);
    Page<Assessment> searchAssessments(Pageable pageable, String searchString);
    Assessment createAssessment(Assessment assessment);
    Assessment updateAssessment(Assessment assessment);
}
