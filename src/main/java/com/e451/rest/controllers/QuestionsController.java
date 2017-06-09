package com.e451.rest.controllers;

import com.e451.rest.domains.question.Question;
import com.e451.rest.domains.question.QuestionResponse;
import com.e451.rest.services.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by e384873 on 6/9/2017.
 */
@RestController
@RequestMapping("/questions")
public class QuestionsController {

    private QuestionService questionService;

    @Autowired
    public QuestionsController(QuestionService questionService) {
        this.questionService = questionService;
    }

    private final Logger logger = LoggerFactory.getLogger(QuestionsController.class);

    @GetMapping()
    public ResponseEntity<QuestionResponse> getQuestions() {
        QuestionResponse questionResponse = new QuestionResponse();

        logger.info("getQuestions request received");

        try {
            questionResponse.setQuestions(questionService.getQuestions());
            logger.info("getQuestions request processed");
        } catch(Exception ex) {
            logger.error("getQuestions encountered ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(questionResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String id) {
        QuestionResponse questionResponse = new QuestionResponse();

        logger.info("getQuestion request received");

        try {
            questionResponse.setQuestions(Arrays.asList(questionService.getQuestion(id)));
            logger.info("getQuestion request processed");
        } catch (Exception ex) {
            logger.error("getQuestion encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(questionResponse);
    }

    @PostMapping()
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody Question question) {
        QuestionResponse questionResponse = new QuestionResponse();

        logger.info("createQuestion request received");

        try {
            questionResponse.setQuestions(Arrays.asList(questionService.createQuestion(question)));
            logger.info("createQuestion request processed");
        } catch (Exception ex) {
            logger.error("createQuestion encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(questionResponse);
    }

    @PutMapping()
    public ResponseEntity updateQuestion(@RequestBody Question question) {
        QuestionResponse questionResponse = new QuestionResponse();

        logger.info("updateQuestion request received");

        try {
            questionResponse.setQuestions(Arrays.asList(questionService.updateQuestion(question)));
            logger.info("updateQuestion request processed");
        } catch (Exception ex) {
            logger.error("updateQuestion encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@PathVariable String id) {
        logger.info("deleteQuestion request received");

        try {
            questionService.deleteQuestion(id);
            logger.info("deleteQuestion request processed");
        } catch (Exception ex) {
            logger.error("deleteQuestion encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
}
