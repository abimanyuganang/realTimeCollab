package com.editor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class DocumentManager {
    private static final DocumentManager instance = new DocumentManager();
    private static final Map<String, String> documents = new ConcurrentHashMap<>();
    private static final String DEFAULT_DOC = "main";

    private DocumentManager() {
        // Initialize default document if needed
        documents.putIfAbsent(DEFAULT_DOC, "");
    }

    public static DocumentManager getInstance() {
        return instance;
    }

    public void createDocument(String title) {
        documents.putIfAbsent(title, "");
    }

    public void updateDocument(String title, String content) {
        documents.put(title, content);
    }

    public String getDocument(String title) {
        return documents.getOrDefault(title, "");
    }

    public Map<String, String> getAllDocuments() {
        return documents;
    }
}