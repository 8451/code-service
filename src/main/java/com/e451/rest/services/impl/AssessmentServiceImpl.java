package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.UserService;
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
    private AuthService authService;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository, AuthService authService) {
        this.assessmentRepository = assessmentRepository;
        this.authService = authService;
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
    @SuppressWarnings("Duplicates")
    public Assessment createAssessment(Assessment assessment) {

        assessment.setCreatedDate(new Date());
        assessment.setModifiedDate(new Date());

        assessment.setCreatedBy(authService.getActiveUser().getUsername());
        assessment.setModifiedBy(authService.getActiveUser().getUsername());

        return assessmentRepository.insert(assessment);
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) {
        assessment.setModifiedDate(new Date());
        assessment.setModifiedBy(authService.getActiveUser().getUsername());

        return assessmentRepository.save(assessment);
    }
}
