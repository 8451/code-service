package com.e451.rest.domains.assessment;

import java.util.List;

/**
 * Created by j747951 on 6/29/2017.
 */
public class QuestionAnswerResponse {
    List<QuestionAnswer> questionAnswers;

    public QuestionAnswerResponse() {
    }

    public List<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}
