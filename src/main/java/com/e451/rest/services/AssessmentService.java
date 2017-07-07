package com.e451.rest.services;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentState;

import java.util.List;

/**
 * Created by j747951 on 6/15/2017.
 */
public interface AssessmentService {
    List<Assessment> getAssessments();
    Assessment getAssessmentByGuid(String guid);
    AssessmentState getAssessmentStateByGuid(String guid);
    Assessment createAssessment(Assessment assessment);
    Assessment updateAssessment(Assessment assessment);
}
