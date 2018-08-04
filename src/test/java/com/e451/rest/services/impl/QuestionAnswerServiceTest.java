package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.domains.assessment.QuestionAnswerResponse;
import com.e451.rest.repositories.AssessmentRepository;
import com.e451.rest.services.AssessmentService;
import com.e451.rest.services.QuestionAnswerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/29/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionAnswerServiceTest {

    private QuestionAnswerService questionAnswerService;
    private List<QuestionAnswer> questionAnswerList;
    private Assessment assessment;

    @Mock
    private AssessmentService assessmentService;

    @Before
    public void setup() {
        this.questionAnswerService = new QuestionAnswerServiceImpl(assessmentService);
        this.assessment = new Assessment("id1", "first", "last", "email");
    }

    @Test
    public void whenCreateQuestionAnswer_returnNewQuestionAnswer() {
        QuestionAnswer questionAnswer = new QuestionAnswer("t1", "b1", null, "qId1", "java");

        when(assessmentService.getAssessmentByGuid("guid1")).thenReturn(assessment);
        when(assessmentService.updateAssessment(assessment)).thenReturn(assessment);

        QuestionAnswer result = questionAnswerService.createQuestionAnswer(questionAnswer, "guid1");

        Assert.assertEquals(1, assessment.getQuestionAnswers().size());
        Assert.assertEquals(questionAnswer.getQuestionResponseId(), result.getQuestionResponseId());
    }

    @Test
    public void whenUpdateQuestionAnswer_returnUpdatedQuestionAnswer() {
        QuestionAnswer questionAnswer = new QuestionAnswer("t1", "b1", null, "qId1", "java");
        QuestionAnswer updatedQuestionAnswer = new QuestionAnswer("t1", "b1", "a1", "qId1", "java");

        when(assessmentService.getAssessmentByGuid("guid1")).thenReturn(assessment);
        when(assessmentService.updateAssessment(assessment)).thenReturn(assessment);

        questionAnswerService.createQuestionAnswer(questionAnswer, "guid1");

        QuestionAnswer result = questionAnswerService.updateQuestionAnswer(updatedQuestionAnswer, "guid1");

        Assert.assertEquals(1, assessment.getQuestionAnswers().size());
        Assert.assertEquals(questionAnswer.getQuestionResponseId(), result.getQuestionResponseId());
        Assert.assertEquals(updatedQuestionAnswer.getAnswer(), result.getAnswer());
    }
}
