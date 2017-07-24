package com.e451.rest.controllers;

import com.e451.rest.domains.language.LanguageResponse;
import com.e451.rest.domains.question.Question;
import com.e451.rest.domains.question.QuestionResponse;
import com.e451.rest.services.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by e384873 on 6/9/2017.
 */
@RestController
@RequestMapping("/questions")
@CrossOrigin
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

    @GetMapping(params = {"page", "size", "property"})
    public ResponseEntity<QuestionResponse>
        getQuestions(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("property") String property) {

        QuestionResponse questionResponse = new QuestionResponse();
        logger.info("getQuestions() request received");

        try {
            Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.ASC, property)));
            logger.info("getQuestions request processed");
            Page<Question> questions = questionService.getQuestions(pageable);
            questionResponse.setQuestions(questions.getContent());
            questionResponse.setPaginationTotalElements(questions.getTotalElements());
        } catch (Exception e) {
            logger.error("getQuestions encountered error ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(questionResponse);
    }

    @GetMapping(params = {"page", "size", "property", "searchString"})
    public ResponseEntity<QuestionResponse>
    searchQuestions(@RequestParam("page") int page, @RequestParam("size") int size,
                    @RequestParam("property") String property, @RequestParam("searchString") String searchString) {

        QuestionResponse questionResponse = new QuestionResponse();
        logger.info("searchQuestions() request received");

        try {
            Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.ASC, property)));
            logger.info("searchQuestions() request processed");
            Page<Question> questions = questionService.searchQuestions(pageable, searchString, searchString, searchString);
            questionResponse.setQuestions(questions.getContent());
            questionResponse.setPaginationTotalElements(questions.getTotalElements());
        } catch (Exception e) {
            logger.error("searchQuestions() encountered error ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(questionResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String id) {
        QuestionResponse questionResponse = new QuestionResponse();
        Question q = null;
        logger.info("getBody request received");

        try {
            q = questionService.getQuestion(id);
            questionResponse.setQuestions(Arrays.asList(questionService.getQuestion(id)));
            logger.info("getBody request processed");
        } catch (Exception ex) {
            logger.error("getBody encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return q != null ? ResponseEntity.ok(questionResponse) : ResponseEntity.notFound().build();
    }

    @GetMapping("/languages")
    public ResponseEntity<LanguageResponse> getLanguages() {
        LanguageResponse languageResponse = new LanguageResponse();
        logger.info("languages request received");

        try {
            languageResponse.setLanguages(questionService.getLanguages());
            logger.info("languages request processed");
        } catch (Exception ex) {
            logger.error("languages encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(languageResponse);
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

        return ResponseEntity.status(HttpStatus.CREATED).body(questionResponse);
    }

    @PutMapping()
    public ResponseEntity<QuestionResponse> updateQuestion(@RequestBody Question question) {
        QuestionResponse questionResponse = new QuestionResponse();

        logger.info("updateQuestion request received");

        try {
            questionResponse.setQuestions(Arrays.asList(questionService.updateQuestion(question)));
            logger.info("updateQuestion request processed");
        } catch (Exception ex) {
            logger.error("updateQuestion encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.accepted().body(questionResponse);
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

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
