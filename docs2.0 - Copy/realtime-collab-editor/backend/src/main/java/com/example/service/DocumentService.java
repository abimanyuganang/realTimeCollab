package com.example.service;

import com.example.model.Document;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentService {
    private ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();

    public Document createDocument(String title, String content, int version) {
        Document document = new Document(title, content, 1);
        documents.put(title, document);
        return document;
    }

    public Document updateDocument(String id, String content) {
        Document document = documents.get(id);
        if (document != null) {
            document.setContent(content);
            document.setVersion(document.getVersion() + 1);
        }
        return document;
    }

    public Document getDocument(String id) {
        return documents.get(id);
    }

    public void deleteDocument(String id) {
        documents.remove(id);
    }
}