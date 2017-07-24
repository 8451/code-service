package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentState;
import com.e451.rest.domains.email.AssessmentStartEmailMessage;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private MailService mailService;
    private String codeWebAddress;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository, AuthService authService,
                                 MailService mailService, @Value("${code.web-ui-address}") String codeWebAddress) {
        this.assessmentRepository = assessmentRepository;
        this.authService = authService;
        this.mailService = mailService;
        this.codeWebAddress = codeWebAddress;
    }

    @Override
    public List<Assessment> getAssessments() {
        return assessmentRepository.findAll();
    }

    @Override
    public Page<Assessment> getAssessments(Pageable pageable) {
        return assessmentRepository.findAll(pageable);
    }

    @Override
    public Assessment getAssessmentByGuid(String guid) {
        return assessmentRepository.findByInterviewGuid(guid);
    }

    @Override
    public AssessmentState getAssessmentStateByGuid(String guid) {
        return getAssessmentByGuid(guid).getState();
    }

    @Override
    public Page<Assessment> searchAssessments(Pageable pageable, String searchString) {
        return assessmentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                pageable,
                searchString,
                searchString,
                searchString
        );
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Assessment createAssessment(Assessment assessment) {

        assessment.setCreatedDate(new Date());
        assessment.setModifiedDate(new Date());

        assessment.setCreatedBy(authService.getActiveUser().getUsername());
        assessment.setModifiedBy(authService.getActiveUser().getUsername());

        return assessmentRepository.save(assessment);
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) {
        assessment.setModifiedDate(new Date());

        if (authService.isAuthenticated()) {
            assessment.setModifiedBy(authService.getActiveUser().getUsername());
        }

        if (assessment.getState() == AssessmentState.AWAIT_EMAIL) {
            mailService.sendEmail(new AssessmentStartEmailMessage(assessment, codeWebAddress));
            assessment.setState(AssessmentState.IN_PROGRESS);
        }

        return assessmentRepository.save(assessment);
    }
}
