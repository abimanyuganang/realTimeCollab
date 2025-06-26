package com.editor.model;

import java.util.UUID;
import java.time.LocalDateTime;

public class Document {
    private String id;
    private String content;
    private LocalDateTime lastModified;
    private int version;

    public Document() {
        this.id = UUID.randomUUID().toString();
        this.content = "";
        this.lastModified = LocalDateTime.now();
        this.version = 0;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.lastModified = LocalDateTime.now();
        this.version++;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public int getVersion() {
        return version;
    }
}