package com.e451.rest.domains.assessment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by j747951 on 7/5/2017.
 */
public enum AssessmentState {
    NOT_STARTED(0),
    AWAIT_EMAIL(1),
    IN_PROGRESS(2),
    NOTES(3),
    CLOSED(4);

    private Integer state;

    AssessmentState(Integer state) {
        this.state = state;
    }

    @JsonValue
    public Integer getState() {
        return state;
    }

    private void setState(Integer state) {
        this.state = state;
    }
}
