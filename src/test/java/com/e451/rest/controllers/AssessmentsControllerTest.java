package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentResponse;
import com.e451.rest.services.AssessmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/15/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AssessmentsControllerTest {

    private AssessmentsController assessmentsController;

    @Mock
    private AssessmentService assessmentService;

    private List<Assessment> assessments;

    @Before
    public void setup() {
        this.assessmentsController = new AssessmentsController(assessmentService);

        assessments = Arrays.asList(
                new Assessment("1", "fn1", "ln1", "test1@test.com"),
                new Assessment("2", "fn2", "ln2", "test2@test.com"),
                new Assessment("3", "fn3", "ln3", "test3@test.com")
        );
    }

    @Test
    public void whenGetAssessments_returnListOfAssessments() {
        when(assessmentService.getAssessments()).thenReturn(assessments);

        ResponseEntity<AssessmentResponse> response = assessmentsController.getAssessments();

        Assert.assertEquals(3, response.getBody().getAssessments().size());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetSingleAssessment_AssessmentServiceThrowsException_returnsInternalServerError() {
        when(assessmentService.getAssessmentByGuid("1")).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<AssessmentResponse> response = assessmentsController.getAssessmentByGuid("1");

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenGetAssessments_AssessmentServiceThrowsException_returnInternalServerError() {
        when(assessmentService.getAssessments()).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<AssessmentResponse> response = assessmentsController.getAssessments();

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void whenCreateAssessment_returnsListOfSingleAssessment() {
        Assessment assessment = assessments.get(0);

        when(assessmentService.createAssessment(assessment)).thenReturn(assessment);

        ResponseEntity<AssessmentResponse> response = assessmentsController.createAssessment(assessment);

        Assert.assertEquals(1, response.getBody().getAssessments().size());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void whenCreateAssessment_AssessmentServiceThrowsException_returnInternalServerError() {
        Assessment assessment = assessments.get(1);

        when(assessmentService.createAssessment(assessment)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<AssessmentResponse> responseEntity = assessmentsController.createAssessment(assessment);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenUpdateAssessment_returnUpdatedAssessment() {
        Assessment updatedAssessment = assessments.get(0);
        updatedAssessment.setFirstName("FirstName");

        when(assessmentService.updateAssessment(updatedAssessment)).thenReturn(updatedAssessment);

        ResponseEntity<AssessmentResponse> response = assessmentsController.updateAssessment(updatedAssessment);

        Assert.assertEquals(1, response.getBody().getAssessments().size());
        Assert.assertEquals(assessments.get(0), response.getBody().getAssessments().get(0));
        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void whenUpdateAssessment_AssessmentServiceThrowsException_returnsInternalServerError() {
        when(assessmentService.updateAssessment(null)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<AssessmentResponse> response = assessmentsController.updateAssessment(null);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
