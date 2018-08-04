package com.e451.rest.domains.assessment.events;

import java.util.Date;

/**
 * Created by j747951 on 6/29/2017.
 */
public class WebSocketEvent {
    private Date timestamp;

    public WebSocketEvent() {
        timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
