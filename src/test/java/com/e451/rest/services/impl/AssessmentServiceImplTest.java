package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentState;
import com.e451.rest.domains.email.AssessmentStartEmailMessage;
import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.MailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/15/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AssessmentServiceImplTest {

    private AssessmentService assessmentService;
    private List<Assessment> assessments;
    private User testUser;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AuthService authService;

    @Mock
    private MailService mailService;

    @Before
    public void setUp() {
        this.assessmentService = new AssessmentServiceImpl(assessmentRepository, authService, mailService,
                "code/web/ui");
        assessments = Arrays.asList(
                new Assessment("1", "fn1", "ln1", "test1@test.com"),
                new Assessment("2", "fn2", "ln2", "test2@test.com"),
                new Assessment("3", "fn3", "ln3", "test3@test.com")
        );

        testUser = new User("id1", "fname", "lname", "email", "Password1!");

        when(authService.getActiveUser()).thenReturn(testUser);
    }

    @Test
    public void whenGetAssessmentsReturnListOfAssessments() {
        when(assessmentRepository.findAll()).thenReturn(assessments);

        List<Assessment> result = assessmentService.getAssessments();

        Assert.assertEquals(assessments.size(), result.size());
    }

    @Test
    public void whenGetAssessmentsPageableReturnListOfAssessments() {
        Pageable pageable = new PageRequest(0, 20);
        Page<Assessment> assessments = new PageImpl(this.assessments);
        when(assessmentRepository.findAll(pageable)).thenReturn(assessments);

        Page<Assessment> actualAssessments = assessmentService.getAssessments(pageable);

        Assert.assertEquals(assessments.getTotalElements(), actualAssessments.getTotalElements());
        Assert.assertEquals(assessments.getContent().size(), actualAssessments.getContent().size());
    }

    @Test
    public void whenGetAssessmentReturnListOfSingleAssessment() {
        when(assessmentRepository.findByInterviewGuid(assessments.get(0).getInterviewGuid())).thenReturn(this.assessments.get(0));

        Assessment result = assessmentService.getAssessmentByGuid(assessments.get(0).getInterviewGuid());

        Assert.assertEquals(assessments.get(0), result);

    }

    @Test
    public void whenGetAssessmentStateReturnAssessmentState() {
        when(assessmentRepository.findByInterviewGuid(assessments.get(0).getInterviewGuid())).thenReturn(this.assessments.get(0));

        AssessmentState result = assessmentService.getAssessmentStateByGuid(assessments.get(0).getInterviewGuid());

        Assert.assertEquals(assessments.get(0).getState(), result);
    }

    @Test
    public void whenSearchAssessmentsReturnListOfAssessments() {
        Pageable pageable = new PageRequest(0, 20);
        Page<Assessment> assessments = new PageImpl(this.assessments);
        when(assessmentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                any(Pageable.class),
                any(String.class),
                any(String.class),
                any(String.class)
        )).thenReturn(assessments);

        Page<Assessment> actualAssessments = assessmentService.searchAssessments(pageable, "");

        Assert.assertEquals(assessments.getTotalElements(), actualAssessments.getTotalElements());
        Assert.assertEquals(assessments.getContent().size(), actualAssessments.getContent().size());
    }

    @Test
    public void whenCreateAssessmentReturnNewAssessment() {
        Assessment assessment = new Assessment("1", "f1", "l1", "test1@test.com");

        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        when(authService.getActiveUser()).thenReturn(testUser);

        Assessment result = assessmentService.createAssessment(assessment);

        Assert.assertEquals(assessment, result);
        Assert.assertEquals(testUser.getUsername(), result.getCreatedBy());
        Assert.assertEquals(testUser.getUsername(), result.getModifiedBy());
        Assert.assertNotNull(assessment.getCreatedDate());
        Assert.assertNotNull(assessment.getModifiedDate());

    }

    @Test
    public void whenUpdateAssessmentReturnUpdatedAssessment() {
        Assessment assessment = new Assessment("1", "f11", "l11", "test11@test.com");

        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        when(authService.getActiveUser()).thenReturn(testUser);
        when(authService.isAuthenticated()).thenReturn(true);

        Assessment result = assessmentService.updateAssessment(assessment);

        Assert.assertEquals(assessment, result);
        Assert.assertEquals(testUser.getUsername(), result.getModifiedBy());
        Assert.assertNotNull(assessment.getModifiedDate());
    }

    @Test
    public void whenUpdateAssessmentNotActiveDoesNotCallMailService() {
        Assessment assessment = new Assessment("1", "f1", "l1", "test@test.com");
        assessment.setState(AssessmentState.IN_PROGRESS);

        Assessment result = assessmentService.updateAssessment(assessment);

        verify(mailService, times(0)).sendEmail(any(DirectEmailMessage.class));
    }

    @Test
    public void whenUpdateAssessmentActiveCallsMailServiceWithAssessmentStartEmail() {
        Assessment assessment = new Assessment("1", "f1", "f2", "test@test.com");
        assessment.setState(AssessmentState.AWAIT_EMAIL);

        Assessment result = assessmentService.updateAssessment(assessment);

        verify(mailService, times(1)).sendEmail(any(AssessmentStartEmailMessage.class));
    }

    @Test
    public void whenGetAssessmentCsvReturnAssessmentStream() {
        Assessment assessment = new Assessment("1", "fn1", "ln1", "test@test.com");

        when(assessmentService.getAssessments()).thenReturn(Arrays.asList(assessment));

        Stream<String> stream = assessmentService.getAssessmentsCsv();

        List<String> strings = stream.collect(Collectors.toList());

        Assert.assertEquals(2, strings.size());
        Assert.assertEquals(Assessment.CSV_HEADERS, strings.get(0));
        Assert.assertEquals(assessment.toCsvRow(), strings.get(1));
    }

}
