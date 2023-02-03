package com.example.androidstudiobbdd;

public class Comment {

    // ! Attributes
    private String title;
    private String description;

    // ! Constructors
    public Comment() {
    }

    public Comment(String title, String description) {
        this.setTitle(title);
        this.setDescription(description);
    }


    // ! Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
