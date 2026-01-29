package com.example.logging_demo.model;

import java.util.List;

public class Question {
    private String text;
    private List<AnswerOption> options;

    public Question(String text, List<AnswerOption> options) {
        this.text = text;
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public List<AnswerOption> getOptions() {
        return options;
    }
}
