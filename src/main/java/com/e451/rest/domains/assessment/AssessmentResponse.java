package com.e451.rest.domains.assessment;

import java.util.List;

/**
 * Created by j747951 on 6/15/2017.
 */
public class AssessmentResponse {

    private List<Assessment> assessments;

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }
}
