package com.example.logging_demo.model;

import java.util.Map;

public class AnswerOption {
    private String text;
    private Map<Person, Integer> points;

    public AnswerOption(String text, Map<Person, Integer> points) {
        this.text = text;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public Map<Person, Integer> getPoints() {
        return points;
    }
}
