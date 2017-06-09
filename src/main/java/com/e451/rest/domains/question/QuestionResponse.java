package com.e451.rest.domains.question;

import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
public class QuestionResponse {
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
