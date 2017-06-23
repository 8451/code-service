package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by j747951 on 6/15/2017.
 */
@Service
public class AssessmentServiceImpl implements AssessmentService {

    private AssessmentRepository assessmentRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public List<Assessment> getAssessments() {
        return assessmentRepository.findAll();
    }

    @Override
    public Assessment getAssessmentByGuid(String guid) {
        return assessmentRepository.findByInterviewGuid(guid);
    }

    @Override
    public Assessment createAssessment(Assessment assessment) {

        assessment.setCreatedDate(new Date());
        assessment.setModifiedDate(new Date());

        // TODO: populate user property.

        return assessmentRepository.insert(assessment);
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) {
        assessment.setModifiedDate(new Date());
        return assessmentRepository.save(assessment);
    }
}
