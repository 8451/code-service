package com.e451.rest.repositories;

import com.e451.rest.domains.question.Question;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionRepositoryIT {

    @Autowired
    private QuestionRepository questionRepository;

    private List<Question> questions;

    @Before
    public void setup() {
        questions = Arrays.asList(
                new Question(null, "What is OOP?", "fun!", "OOP", 5),
                new Question(null, "What is fxnl programming?", "more fun!", "Functional", 1),
                new Question(null, "What is scrum?", "even MORE fun!", "Agile", 3)
        );
        questions = questionRepository.insert(questions);
    }

    @After
    public void teardown() {
        questionRepository.deleteAll();
    }

    @Test
    public void getAllQuestions_returnsAllQuestions() {
        List<Question> questions = questionRepository.findAll();

        Assert.assertEquals(questions.size(), 3);
    }

    @Test
    public void getQuestion_returnSingleQuestion() {
        Question question = questionRepository.findOne(questions.get(0).getId());

        Assert.assertEquals(question, questions.get(0));
    }

    @Test
    public void deleteQuestion_deletesQuestion() {
         questionRepository.delete(questions.get(1).getId());

         Assert.assertEquals(questionRepository.findAll().size(), 2);
    }

    @Test
    public void updateQuestion_returnsUpdatedQuestion() {
        Question question = questions.get(1);

        question.setQuestion("Does this work?");

        Question updatedQuestion = questionRepository.save(question);

        Assert.assertEquals(updatedQuestion, question);
    }
}
