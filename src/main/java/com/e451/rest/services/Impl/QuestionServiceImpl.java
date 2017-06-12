package com.e451.rest.services.Impl;

import com.e451.rest.domains.question.Question;
import com.e451.rest.repositories.QuestionRepository;
import com.e451.rest.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestion(String id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Question createQuestion(Question question) {
        question.setCreatedDate(new Date());
        question.setModifiedDate(new Date());

        return questionRepository.insert(question);
    }

    @Override
    public Question updateQuestion(Question question) {
        question.setModifiedDate(new Date());
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(String id) {
        questionRepository.delete(id);
    }
}
