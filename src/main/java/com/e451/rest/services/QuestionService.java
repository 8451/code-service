package com.e451.rest.services;

import com.e451.rest.domains.question.Question;

import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
public interface QuestionService {
    List<Question> getQuestions();
    Question getQuestion(String id);
    Question createQuestion(Question question);
    Question updateQuestion(Question question);
    void deleteQuestion(String id);
}
