package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.domains.assessment.events.AnswerQuestionEvent;
import com.e451.rest.domains.assessment.events.NewQuestionEvent;
import com.e451.rest.services.QuestionAnswerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/29/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AssessmentsSocketControllerTest {

    private AssessmentsSocketController assessmentsSocketController;

    @Mock
    private QuestionAnswerService questionAnswerService;

    @Before
    public void setUp() {
        assessmentsSocketController = new AssessmentsSocketController(questionAnswerService);
    }

    @Test
    public void whenCreateQuestionAnswerReturnNewQuestionAnswer() {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        NewQuestionEvent newQuestionEvent = new NewQuestionEvent("Q1", "body1", null);

        when(questionAnswerService.createQuestionAnswer(any(QuestionAnswer.class), any(String.class))).thenReturn(questionAnswer);

        NewQuestionEvent response = this.assessmentsSocketController
                .newQuestion("1", newQuestionEvent);

        Assert.assertEquals(newQuestionEvent.getTitle(), response.getTitle());
        Assert.assertEquals(newQuestionEvent.getBody(), response.getBody());
        Assert.assertNotNull(response.getQuestionResponseId());
    }

    @Test
    public void whenUpdateQuestionAnswerReturnUpdatedQuestionAnswer() {
        QuestionAnswer questionAnswer = new QuestionAnswer();


        when(questionAnswerService.updateQuestionAnswer(any(QuestionAnswer.class), any(String.class))).thenReturn(questionAnswer);

        AnswerQuestionEvent answerQuestionEvent = new AnswerQuestionEvent("Q1", "body1", "answer1", "1");

        AnswerQuestionEvent response = this.assessmentsSocketController.answerQuestion("1", answerQuestionEvent);

        Assert.assertEquals(answerQuestionEvent.getTitle(), response.getTitle());
        Assert.assertEquals(answerQuestionEvent.getBody(), response.getBody());
    }
}
