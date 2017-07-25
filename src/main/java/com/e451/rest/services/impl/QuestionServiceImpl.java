package com.e451.rest.services.impl;

import com.e451.rest.domains.question.Question;
import com.e451.rest.repositories.QuestionRepository;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepository;
    private AuthService authService;

    private static final List<String> languages = Arrays.asList("Java", "Python2", "Python3", "SQL", "C#", "C", "C++",
            "Powershell", "Bash", "Javascript", "Typescript", "Ruby", "PHP", "Scala");

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, AuthService authService) {
        this.questionRepository = questionRepository;
        this.authService = authService;
    }

    @Override
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public Page<Question> searchQuestions(Pageable pageable, String searchString) {
        return questionRepository.findQuestionsByTitleContainsIgnoreCaseOrLanguageContainsIgnoreCaseOrCreatedByContainsIgnoreCase(
                pageable, searchString, searchString, searchString);
    }

    @Override
    public Question getQuestion(String id) {
        return questionRepository.findOne(id);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Question createQuestion(Question question) {
        question.setCreatedDate(new Date());
        question.setModifiedDate(new Date());

        question.setCreatedBy(authService.getActiveUser().getUsername());
        question.setModifiedBy(authService.getActiveUser().getUsername());

        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question) {
        question.setModifiedDate(new Date());
        question.setModifiedBy(authService.getActiveUser().getUsername());
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(String id) {
        questionRepository.delete(id);
    }

    @Override
    public List<String> getLanguages() {
        Collections.sort(languages);
        return languages;
    }
}
