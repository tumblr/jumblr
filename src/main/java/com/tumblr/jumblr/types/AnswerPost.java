package com.tumblr.jumblr.types;

public class AnswerPost extends Post {

    // @TODO make editable

    private String asking_name, asking_url;
    private String question;
    private String answer;

    public String getAskingUrl() {
        return asking_url;
    }

    public String getAskingName() {
        return asking_name;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
