package com.example.model;

public class Document {
    private String title;
    private String content;
    private int version;

    public Document() {
    }

    public Document(String title, String content, int version) {
        this.title = title;
        this.content = content;
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    
}