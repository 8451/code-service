package com.e451.rest.controllers;

import com.e451.rest.domains.question.Question;
import com.e451.rest.domains.question.QuestionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by e384873 on 6/9/2017.
 */
@RestController
@RequestMapping("/questions")
public class QuestionController {
    @GetMapping()
    public ResponseEntity<QuestionResponse> getQuestions() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String id) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody Question question) {
        return null;
    }

    @PutMapping()
    public ResponseEntity updateQuestion(@RequestBody Question question) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@PathVariable String id) {
        return null;
    }
}
