package com.e451.rest.services.impl;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.services.AssessmentService;
import com.e451.rest.services.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by j747951 on 6/29/2017.
 */
@Service
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

    private AssessmentService assessmentService;

    @Autowired
    public QuestionAnswerServiceImpl(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @Override
    public QuestionAnswer createQuestionAnswer(QuestionAnswer questionAnswer, String assessmentGuid) {
        Assessment assessment = assessmentService.getAssessmentByGuid(assessmentGuid);
        assessment.getQuestionAnswers().add(questionAnswer);

        assessmentService.updateAssessment(assessment);

        return questionAnswer;
    }

    @Override
    public QuestionAnswer updateQuestionAnswer(QuestionAnswer questionAnswer, String assessmentGuid) {
        Assessment assessment = assessmentService.getAssessmentByGuid(assessmentGuid);
        int indexOfAnswer = assessment.getQuestionAnswers().size() - 1;

        for(; indexOfAnswer >= 0; indexOfAnswer--) {
            if(assessment.getQuestionAnswers().get(indexOfAnswer).getQuestionResponseId().equals(questionAnswer.getQuestionResponseId())) {
                break;
            }
        }

        assessment.getQuestionAnswers().set(indexOfAnswer, questionAnswer);

        assessmentService.updateAssessment(assessment);

        return questionAnswer;
    }
}
