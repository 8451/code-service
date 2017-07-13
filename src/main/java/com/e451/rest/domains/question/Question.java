package com.e451.rest.domains.question;

import com.e451.rest.domains.user.User;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by e384873 on 6/9/2017.
 */
public class Question {
    @Id
    private String id;
    private String body;
    private String suggestedAnswer;
    private String title;
    private String createdBy;
    private String modifiedBy;
    private Date createdDate;
    private Date modifiedDate;
    private Integer difficulty;
    private String language;

    public Question() {

    }

    public Question(String id, String body, String suggestedAnswer, String title, Integer difficulty) {
        this.id = id;
        this.body = body;
        this.suggestedAnswer = suggestedAnswer;
        this.title = title;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSuggestedAnswer() {
        return suggestedAnswer;
    }

    public void setSuggestedAnswer(String suggestedAnswer) {
        this.suggestedAnswer = suggestedAnswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        if (!id.equals(question1.id)) return false;
        if (!body.equals(question1.body)) return false;
        if (!suggestedAnswer.equals(question1.suggestedAnswer)) return false;
        if (!title.equals(question1.title)) return false;
        if (createdBy != null ? !createdBy.equals(question1.createdBy) : question1.createdBy != null) return false;
        if (createdDate != null ? !createdDate.equals(question1.createdDate) : question1.createdDate != null)
            return false;
        if (modifiedBy != null ? !modifiedBy.equals(question1.modifiedBy) : question1.modifiedBy != null) return false;
        if (modifiedDate != null ? !modifiedDate.equals(question1.modifiedDate) : question1.modifiedDate != null)
            return false;
        if (!difficulty.equals(question1.difficulty)) return false;
        return language.equals(question1.language);
    }

}
