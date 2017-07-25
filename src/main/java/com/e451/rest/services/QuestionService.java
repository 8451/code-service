package com.e451.rest.services;

import com.e451.rest.domains.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
public interface QuestionService {
    List<Question> getQuestions();
    Page<Question> getQuestions(Pageable pageable);
    Page<Question> searchQuestions(Pageable pageable, String searchString);
    Question getQuestion(String id);
    Question createQuestion(Question question);
    Question updateQuestion(Question question);
    void deleteQuestion(String id);
    List<String> getLanguages();
}
