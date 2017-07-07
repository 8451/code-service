package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.AssessmentResponse;
import com.e451.rest.domains.assessment.AssessmentState;
import com.e451.rest.domains.assessment.AssessmentStateResponse;
import com.e451.rest.services.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

/**
 * Created by j747951 on 6/15/2017.
 */
@Controller
@RequestMapping("/assessments")
@CrossOrigin
public class AssessmentsController {

    private AssessmentService assessmentService;
    private static final Logger logger = LoggerFactory.getLogger(AssessmentsController.class);

    @Autowired
    public AssessmentsController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping
    public ResponseEntity<AssessmentResponse> getAssessments() {
        AssessmentResponse response = new AssessmentResponse();

        logger.info("getAssessments request received");

        try {
            response.setAssessments(assessmentService.getAssessments());
            logger.info("getAssessments request processed");
        } catch (Exception ex) {
            logger.error("getAssessments encountered error", error);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<AssessmentResponse> getAssessmentByGuid(@PathVariable String guid) {
        AssessmentResponse assessmentResponse = new AssessmentResponse();
        Assessment assessment = null;
        logger.info("getAssessment request received");

        try {
            assessment = assessmentService.getAssessmentByGuid(guid);
            assessmentResponse.setAssessments(Arrays.asList(assessment));
            logger.info("getAssessment request processed");
        } catch (Exception ex) {
            logger.error("getAssessment encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return assessment != null ? ResponseEntity.ok(assessmentResponse) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{guid}/status")
    public ResponseEntity<AssessmentStateResponse> getAssessmentStateByGuid(@PathVariable String guid) {
        AssessmentStateResponse assessmentStateResponse = new AssessmentStateResponse();
        AssessmentState assessmentState = null;
        logger.info("getAssessmentState request received");

        try {
            assessmentState = assessmentService.getAssessmentStateByGuid(guid);
            assessmentStateResponse.setState(assessmentState);
            logger.info("getAssessmentState request processed");
        } catch (Exception e){
            logger.error("getAssessmentState encountered error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return assessmentState != null ? ResponseEntity.ok(assessmentStateResponse) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AssessmentResponse> createAssessment(@RequestBody Assessment assessment) {
        AssessmentResponse response = new AssessmentResponse();

        logger.info("createAssessment request received");

        try {
            response.setAssessments(Arrays.asList(assessmentService.createAssessment(assessment)));
            logger.info("createAssessment request processed");
        } catch (Exception ex) {
            logger.error("createAssessment encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping()
    public ResponseEntity<AssessmentResponse> updateAssessment(@RequestBody Assessment assessment) {
        AssessmentResponse assessmentResponse = new AssessmentResponse();

        logger.info("updateAssessment request received");

        try {
            assessmentResponse.setAssessments(Arrays.asList(assessmentService.updateAssessment(assessment)));
            logger.info("updateAssessment request processed");
        } catch (Exception ex) {
            logger.error("updateAssessment encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.accepted().body(assessmentResponse);
    }
}
