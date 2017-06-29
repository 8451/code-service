package com.e451.rest.domains.email;

import com.e451.rest.domains.assessment.Assessment;

/**
 * Created by j747951 on 6/28/2017.
 */
public class AssessmentStartEmailMessage extends DirectEmailMessage {

    private Assessment assessment;
    private String codeWebAddress;

    public AssessmentStartEmailMessage(Assessment assessment, String codeWebAddress) {
        this.assessment = assessment;
        this.codeWebAddress = codeWebAddress;
        super.setHtml(false);
        super.setPriority(1);
        super.setTo(new String[] {assessment.getEmail()});
        super.setSubject("Your interview awaits");
    }

    @Override
    public String getBody() {
        StringBuilder builder = new StringBuilder();
        builder.append("Hi " + assessment.getFirstName() + "!\n\n")
                .append("We're really excited to have you interview with us! You can access the interview at:\n")
                .append(String.format("%s/candidate/%s\n\n", codeWebAddress, assessment.getInterviewGuid()))
                .append("We hope you enjoy your interview experience!");

        return builder.toString();
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public String getCodeWebAddress() {
        return codeWebAddress;
    }

    public void setCodeWebAddress(String codeWebAddress) {
        this.codeWebAddress = codeWebAddress;
    }
}
