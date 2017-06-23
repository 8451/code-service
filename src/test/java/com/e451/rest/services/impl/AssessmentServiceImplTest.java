package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/15/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AssessmentServiceImplTest {

    private AssessmentService assessmentService;
    private List<Assessment> assessments;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Before
    public void setup() {
        this.assessmentService = new AssessmentServiceImpl(assessmentRepository);
        assessments = Arrays.asList(
                new Assessment("1", "fn1", "ln1", "test1@test.com"),
                new Assessment("2", "fn2", "ln2", "test2@test.com"),
                new Assessment("3", "fn3", "ln3", "test3@test.com")
        );
    }

    @Test
    public void whenGetAssessments_returnListOfAssessments() {
        when(assessmentRepository.findAll()).thenReturn(assessments);

        List<Assessment> result = assessmentService.getAssessments();

        Assert.assertEquals(assessments.size(), result.size());
    }

    @Test
    public void whenGetAssessment_returnListOfSingleAssessment() {
        when(assessmentRepository.findByInterviewGuid(assessments.get(0).getInterviewGuid())).thenReturn(this.assessments.get(0));

        Assessment result = assessmentService.getAssessmentByGuid(assessments.get(0).getInterviewGuid());

        Assert.assertEquals(assessments.get(0), result);

    }

    @Test
    public void whenCreateAssessment_returnNewAssessment() {
        Assessment assessment = new Assessment("1", "f1", "l1", "test1@test.com");

        when(assessmentRepository.insert(assessment)).thenReturn(assessment);

        Assessment result = assessmentService.createAssessment(assessment);

        Assert.assertEquals(assessment, result);
        Assert.assertNotNull(assessment.getCreatedDate());
        Assert.assertNotNull(assessment.getModifiedDate());

        // TODO: add user properties.
    }

    @Test
    public void whenUpdateQuestion_returnUpdatedQuestion()  {
        Assessment assessment = new Assessment("1", "f11", "l11", "test11@test.com");

        when(assessmentRepository.save(assessment)).thenReturn(assessment);

        Assessment result = assessmentService.updateAssessment(assessment);

        Assert.assertEquals(assessment, result);
        Assert.assertNotNull(assessment.getModifiedDate());

        // TODO: make sure the user is not null.
    }

}
