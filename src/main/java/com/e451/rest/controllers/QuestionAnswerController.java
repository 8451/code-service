package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.domains.assessment.QuestionAnswerResponse;
import com.e451.rest.services.QuestionAnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by j747951 on 6/29/2017.
 */
@Controller
@RequestMapping("/assessments/{guid}/answers")
public class QuestionAnswerController {

    private QuestionAnswerService questionAnswerService;
    private static final Logger LOG = LoggerFactory.getLogger(QuestionAnswerController.class);

    @Autowired
    public QuestionAnswerController(QuestionAnswerService questionAnswerService) {
        this.questionAnswerService = questionAnswerService;
    }

    @PostMapping
    public ResponseEntity<QuestionAnswerResponse> createQuestionAnswer(@RequestBody QuestionAnswer questionAnswer,
                                                                       @PathVariable("guid") String assessmentGuid) {
        QuestionAnswerResponse response = new QuestionAnswerResponse();

        LOG.info("createQuestionAnswer request received");

        try {
            response.setQuestionAnswers(
                    Arrays.asList(questionAnswerService.createQuestionAnswer(questionAnswer, assessmentGuid)));
            LOG.info("createQuestionAnswer request received");
        } catch (Exception ex) {
            LOG.info("createQuestionAnswer encountered error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<QuestionAnswerResponse> updateQuestionAnswer(@RequestBody QuestionAnswer questionAnswer,
                                                                       @PathVariable("guid") String assessmentGuid) {
        QuestionAnswerResponse response = new QuestionAnswerResponse();

        LOG.info("createQuestionAnswer request received");

        try {
            response.setQuestionAnswers(
                    Arrays.asList(questionAnswerService.updateQuestionAnswer(questionAnswer, assessmentGuid)));
            LOG.info("createQuestionAnswer request received");
        } catch (Exception ex) {
            LOG.info("createQuestionAnswer encountered error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
