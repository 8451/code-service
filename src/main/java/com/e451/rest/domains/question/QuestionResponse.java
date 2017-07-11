package com.e451.rest.domains.question;

import java.util.List;

/**
 * Created by e384873 on 6/9/2017.
 */
public class QuestionResponse {
    private List<Question> questions;
    private Long paginationTotalElements = 0L;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Long getPaginationTotalElements() {
        return paginationTotalElements;
    }

    public void setPaginationTotalElements(Long paginationTotalElements) {
        this.paginationTotalElements = paginationTotalElements;
    }
}
