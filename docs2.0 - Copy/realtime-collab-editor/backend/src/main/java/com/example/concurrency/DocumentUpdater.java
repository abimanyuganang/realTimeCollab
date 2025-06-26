package com.example.concurrency;

import com.example.service.DocumentService;

public class DocumentUpdater implements Runnable {
    private final DocumentService service;
    private final String docId;
    private final String newContent;

    public DocumentUpdater(DocumentService service, String docId, String newContent) {
        this.service = service;
        this.docId = docId;
        this.newContent = newContent;
    }

    @Override
    public void run() {
        service.updateDocument(docId, newContent);
        System.out.println("Updated document " + docId + " in thread " + Thread.currentThread().getName());
    }
}