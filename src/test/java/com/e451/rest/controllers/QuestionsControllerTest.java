package com.e451.rest.controllers;

import com.e451.rest.domains.question.Question;
import com.e451.rest.domains.question.QuestionResponse;
import com.e451.rest.services.QuestionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by e384873 on 6/9/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionsControllerTest {

    private QuestionsController questionsController;

    @Mock
    private QuestionService questionService;

    private List<Question> questions;

    @Before
    public void setup() {

        this.questionsController = new QuestionsController(questionService);
        questions = Arrays.asList(
                new Question("1", "q1", "a1", "t1", 1),
                new Question("2", "q2", "a2", "t2", 2),
                new Question("3", "q3", "a3", "t3", 3));
    }

    @Test
    public void whenGetQuestions_returnListOfQuestions() {
        when(questionService.getQuestions()).thenReturn(questions);

        ResponseEntity<QuestionResponse> response = questionsController.getQuestions();

        Assert.assertEquals(3, response.getBody().getQuestions().size());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetQuestionsPageable_returnListOfQuestions() {
        Pageable page = new PageRequest(0, 20);
        Page pageResponse = new PageImpl<Question>(this.questions);
        when(questionService.getQuestions(any())).thenReturn(pageResponse);

        ResponseEntity<QuestionResponse> response = questionsController.getQuestions(0, 20, "title");

        Assert.assertEquals(this.questions.size(), response.getBody().getQuestions().size());
        Assert.assertEquals(pageResponse.getTotalElements(), (long) response.getBody().getTotalElements());
    }

    @Test
    public void whenGetQuestionException_QuestionServiceThrowsException_returnsInternalServerError() {
        when(questionService.getQuestions()).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionResponse> response = questionsController.getQuestions();

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenGetSingleQuestion_returnListOfSingleQuestion() {
        String id = "1";

        when(questionService.getQuestion(id)).thenReturn(questions.get(0));

        ResponseEntity<QuestionResponse> response = questionsController.getQuestion(id);

        Assert.assertEquals(1, response.getBody().getQuestions().size());
        Assert.assertEquals(questions.get(0), response.getBody().getQuestions().get(0));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetNonExistentQuestion_ReturnNotFound() {
        final String id = "1";
        when(questionService.getQuestion(id)).thenReturn(null);

        ResponseEntity<QuestionResponse> response = questionsController.getQuestion(id);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void whenGetSingleQuestion_QuestionServiceThrowsException_returnsInternalServerError() {
        when(questionService.getQuestion("1")).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionResponse> response = questionsController.getQuestion("1");

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenUpdateQuestion_returnUpdatedQuestion() {
        Question updatedQuestion = questions.get(0);
        updatedQuestion.setBody("is this a real question?");

        when(questionService.updateQuestion(updatedQuestion)).thenReturn(updatedQuestion);

        ResponseEntity<QuestionResponse> response = questionsController.updateQuestion(updatedQuestion);

        Assert.assertEquals(1, response.getBody().getQuestions().size());
        Assert.assertEquals(questions.get(0), response.getBody().getQuestions().get(0));
        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void whenUpdateQuestion_QuestionServiceThrowsException_returnsInternalServerError() {
        when(questionService.updateQuestion(null)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionResponse> response = questionsController.updateQuestion(null);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenCreateQuestion_returnListOfQuestions() {
        Question newQuestion = new Question("4", "q4", "a4", "t4", 1);

        when(questionService.createQuestion(newQuestion)).thenReturn(newQuestion);

        ResponseEntity<QuestionResponse> response = questionsController.createQuestion(newQuestion);

        Assert.assertEquals(1, response.getBody().getQuestions().size());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void whenCreateQuestion_QuestionServiceThrowsException_returnsInternalServerError() {
        Question newQuestion = new Question("4", "q4", "a4", "t4", 1);

        when(questionService.createQuestion(newQuestion)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<QuestionResponse> response = questionsController.createQuestion(newQuestion);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenDeleteQuestion_returnSuccess()  {

        Mockito.doNothing().when(questionService).deleteQuestion("1");

        ResponseEntity response = questionsController.deleteQuestion("1");

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenDeleteQuestion_QuestionServiceThrowsException_returnsInternalServerError() {
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(questionService).deleteQuestion("1");

        ResponseEntity response = questionsController.deleteQuestion("1");

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }



}
