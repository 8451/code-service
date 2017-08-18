package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.domains.assessment.QuestionAnswerResponse;
import com.e451.rest.domains.question.QuestionResponse;
import com.e451.rest.services.QuestionAnswerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@TestPropertySource("application-test.properties")
@RunWith(MockitoJUnitRunner.class)
public class QuestionAnswerControllerTest {
    private QuestionAnswerController questionAnswerController;

    @Mock
    private QuestionAnswerService questionAnswerService;

    private List<QuestionAnswer> questionAnswers;

    @Before
    public void setup() {
        this.questionAnswerController = new QuestionAnswerController(questionAnswerService);

        this.questionAnswers = Arrays.asList(
                new QuestionAnswer("q1", "b1", "a1", "1"),
                new QuestionAnswer("q2", "b2", "a2", "2"),
                new QuestionAnswer("q3", "b3", "a3", "3")
        );
    }

    @Test
    public void whenCreateQuestionAnswer_returnCreatedQuestionAnswer() {
        when(questionAnswerService.createQuestionAnswer(questionAnswers.get(0), "123")).thenReturn(questionAnswers.get(0));

        ResponseEntity<QuestionAnswerResponse> response = questionAnswerController.createQuestionAnswer(questionAnswers.get(0), "123");

        Assert.assertEquals(1, response.getBody().getQuestionAnswers().size());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void whenCreateQuestionAnswer_QuestionAnswerServiceThrowsException_returnsInternalServerError() {
        when(questionAnswerService.createQuestionAnswer(any(QuestionAnswer.class), any(String.class)))
                .thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionAnswerResponse> response =
                questionAnswerController.createQuestionAnswer(questionAnswers.get(0), "123");

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void whenUpdateQuestionAnswer_returnUpdatedQuestionAnswer() {
        when(questionAnswerService.updateQuestionAnswer(questionAnswers.get(0), "123")).thenReturn(questionAnswers.get(0));

        ResponseEntity<QuestionAnswerResponse> response = questionAnswerController.updateQuestionAnswer(questionAnswers.get(0), "123");

        Assert.assertEquals(1, response.getBody().getQuestionAnswers().size());
        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void whenUpdateQuestionAnswer_QuestionAnswerServiceThrowsException_returnsInternalServerError() {
        when(questionAnswerService.updateQuestionAnswer(any(QuestionAnswer.class), any(String.class)))
                .thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionAnswerResponse> response =
                questionAnswerController.updateQuestionAnswer(questionAnswers.get(0), "123");

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
