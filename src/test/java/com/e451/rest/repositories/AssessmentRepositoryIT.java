package com.e451.rest.repositories;

import com.e451.rest.domains.assessment.Assessment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by l659598 on 6/22/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssessmentRepositoryIT {
    @Autowired
    private AssessmentRepository assessmentRepository;

    private List<Assessment> assessments;

    @Before
    public void setup() {
        assessments = Arrays.asList(
                new Assessment("1", "fn1", "ln1", "test1@test.com"),
                new Assessment("2", "fn2", "ln2", "test2@test.com"),
                new Assessment("3", "fn3", "ln3", "test3@test.com")
        );
        assessments = assessmentRepository.insert(assessments);
    }

    @After
    public void teardown() {
        assessmentRepository.deleteAll();
    }

    @Test
    public void getAllAssessments_returnsAllAssessments() {
        List<Assessment> assessments = assessmentRepository.findAll();

        Assert.assertEquals(assessments.size(), 3);
    }

    @Test
    public void getAssessmentByGuid_returnSingleAssessment() {
        Assessment assessment = assessmentRepository.findByInterviewGuid(assessments.get(0).getInterviewGuid());

        Assert.assertEquals(assessment, assessments.get(0));
    }
//
//    @Test
//    public void deleteAssessment_deletesAssessment() {
//        assessmentRepository.delete(assessments.get(1).getId());
//
//        Assert.assertEquals(assessmentRepository.findAll().size(), 2);
//    }

    @Test
    public void updateAssessment_returnsUpdatedAssessment() {
        Assessment assessment = assessments.get(1);

        assessment.setFirstName("FirstName");

        Assessment updatedAssessment = assessmentRepository.save(assessment);

        Assert.assertEquals(updatedAssessment, assessment);
    }
}
