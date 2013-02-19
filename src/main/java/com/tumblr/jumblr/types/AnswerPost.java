package com.tumblr.jumblr.types;

public class AnswerPost extends Post {

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

    @Override
    public void save() {
        throw new IllegalArgumentException("Cannot save AnswerPost");
    }

}