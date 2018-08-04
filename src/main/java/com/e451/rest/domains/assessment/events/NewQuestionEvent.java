package com.e451.rest.domains.assessment.events;

/**
 * Created by j747951 on 6/29/2017.
 */
public class NewQuestionEvent extends WebSocketEvent {
    private String title;
    private String body;
    private String questionResponseId;
    private String language;

    public NewQuestionEvent() {
        super();
    }

    public NewQuestionEvent(String title, String body, String questionResponseId) {
        super();
        this.title = title;
        this.body = body;
        this.questionResponseId = questionResponseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getQuestionResponseId() {
        return questionResponseId;
    }

    public void setQuestionResponseId(String questionResponseId) {
        this.questionResponseId = questionResponseId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
