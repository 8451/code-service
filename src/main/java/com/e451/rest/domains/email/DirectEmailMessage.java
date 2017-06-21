package com.e451.rest.domains.email;

/**
 * Created by j747951 on 6/20/2017.
 */
public class DirectEmailMessage {

    protected String[] to;
    protected Integer priority;
    protected String subject;
    protected String body;
    protected boolean isHtml;

    public DirectEmailMessage(String[] to, Integer priority, String subject, String body) {
        this.to = to;
        this.priority = priority;
        this.subject = subject;
        this.body = body;
    }

    public DirectEmailMessage() {}

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHtml() {
        return isHtml;
    }

    protected void setHtml(boolean html) {
        isHtml = html;
    }
}
