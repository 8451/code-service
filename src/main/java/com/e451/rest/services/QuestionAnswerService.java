package com.e451.rest.services;

import com.e451.rest.domains.assessment.QuestionAnswer;
import com.e451.rest.domains.question.Question;

/**
 * Created by j747951 on 6/29/2017.
 */
public interface QuestionAnswerService {
    QuestionAnswer createQuestionAnswer(QuestionAnswer questionAnswer, String assessmentGuid);
    QuestionAnswer updateQuestionAnswer(QuestionAnswer questionAnswer, String assessmentGuid);
}
