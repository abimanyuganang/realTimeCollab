package com.editor;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserSession implements Runnable {
    private String userId;
    private String username;
    private String documentId;
    private Thread sessionThread;
    private AtomicBoolean isRunning;
    
    public UserSession(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.isRunning = new AtomicBoolean(false);
        this.sessionThread = new Thread(this);
    }

    public void startSession() {
        isRunning.set(true);
        sessionThread.start();
    }

    public void stopSession() {
        isRunning.set(false);
        sessionThread.interrupt();
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            try {
                // Handle user session activities here
                // Like checking for document updates, syncing changes, etc.
                Thread.sleep(100); // Prevent CPU overload
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}