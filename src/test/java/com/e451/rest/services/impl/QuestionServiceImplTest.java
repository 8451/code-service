package com.e451.rest.services.impl;

import com.e451.rest.domains.question.Question;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.QuestionRepository;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.QuestionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by e384873 on 6/9/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceImplTest {

    private QuestionService questionService;
    private List<Question> questions;
    private User testUser;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AuthService authService;

    @Before
    public void setup() {
        this.questionService = new QuestionServiceImpl(questionRepository, authService);

        questions = Arrays.asList(
                new Question("1", "What is OOP?", "fun!", "OOP", 5),
                new Question("2", "What is fxnl programming?", "more fun!", "Functional", 1),
                new Question("3", "What is scrum?", "even MORE fun!", "Agile", 3)
        );

        testUser = new User("id1", "fname", "lname", "email", "Password1!");
    }

    @Test
    public void whenGetQuestions_returnListOfQuestions() {
        when(questionRepository.findAll()).thenReturn(this.questions);

        List<Question> result = questionService.getQuestions();

        Assert.assertTrue(result.size() == this.questions.size());
    }

    @Test
    public void whenGetQuestion_returnListOfSingleQuestion() {
        String id = "1";

        when(questionRepository.findOne(id)).thenReturn(this.questions.get(0));

        Question result = questionService.getQuestion(id);

        Assert.assertEquals(result, questions.get(0));

    }

    @Test
    public void whenCreateQuestion_returnNewQuestion()  {
        Question q = new Question("4", "What is agile?", "super fun!", "More Agile", 3);

        when(questionRepository.insert(q)).thenReturn(q);
        when(authService.getActiveUser()).thenReturn(testUser);

        Question result = questionService.createQuestion(q);

        Assert.assertEquals(q, result);
        Assert.assertEquals(testUser.getUsername(), q.getCreatedBy());
        Assert.assertEquals(testUser.getUsername(), q.getModifiedBy());
        Assert.assertNotNull(q.getCreatedDate());
        Assert.assertNotNull(q.getModifiedDate());
    }

    @Test
    public void whenUpdateQuestion_returnUpdatedQuestion()  {
        Question q = new Question("3", "What is agile?", "even more fun!", "", 4);

        when(questionRepository.save(q)).thenReturn(q);
        when(authService.getActiveUser()).thenReturn(testUser);

        Question result = questionService.updateQuestion(q);

        Assert.assertEquals(q, result);
        Assert.assertNotNull(q.getModifiedDate());
        Assert.assertEquals(testUser.getUsername(), q.getModifiedBy());

    }

    @Test
    public void whenDeleteQuestion_verifyDeleteCalled() {
        Mockito.doNothing().when(questionRepository).delete("1");

        questionService.deleteQuestion("1");

        verify(questionRepository).delete("1");
    }

}
