package com.e451.rest.services;

import com.e451.rest.domains.assessment.Assessment;

import java.util.List;

/**
 * Created by j747951 on 6/15/2017.
 */
public interface AssessmentService {
    List<Assessment> getAssessments();
    Assessment createAssessment(Assessment assessment);
}
