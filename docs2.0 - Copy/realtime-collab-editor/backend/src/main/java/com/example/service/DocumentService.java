package com.example.service;

import com.example.model.Document;
import com.example.concurrency.LockManager;
import com.example.concurrency.DocumentUpdater;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class DocumentService {
    private ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();
    private final LockManager lockManager = new LockManager();

    public Document createDocument(String title, String content, int version) {
        Document document = new Document(title, content, 1);
        documents.put(title, document);
        return document;
    }

    public Document updateDocument(String id, String content) {
        Lock lock = lockManager.getLock(id);
        lock.lock();
        try {
            Document document = documents.get(id);
            if (document != null) {
                document.setContent(content);
                document.setVersion(document.getVersion() + 1);
            }
            return document;
        } finally {
            lock.unlock();
        }
    }

    public Document getDocument(String id) {
        return documents.get(id);
    }

    public void deleteDocument(String id) {
        documents.remove(id);
    }



    public long countDocumentsWithPrefix(String prefix) {
        return documents.values().parallelStream()
            .filter(doc -> doc.getTitle().startsWith(prefix))
            .count();
    }

    
    public void updateMultipleDocumentsWithJoin(String[] docIds, String[] contents) throws InterruptedException {
        if (docIds.length != contents.length) throw new IllegalArgumentException("Arrays must be the same length");
        Thread[] threads = new Thread[docIds.length];
        for (int i = 0; i < docIds.length; i++) {
            Runnable updater = new DocumentUpdater(this, docIds[i], contents[i]);
            threads[i] = new Thread(updater, "Updater-" + docIds[i]);
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
    }

    
    public void causeDeadlock(String docId1, String docId2, String content1, String content2) throws InterruptedException {
        Runnable task1 = () -> {
            Lock lock1 = lockManager.getLock(docId1);
            Lock lock2 = lockManager.getLock(docId2);
            lock1.lock();
            try {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                lock2.lock();
                try {
                    updateDocument(docId1, content1);
                } finally {
                    lock2.unlock();
                }
            } finally {
                lock1.unlock();
            }
        };

        Runnable task2 = () -> {
            Lock lock1 = lockManager.getLock(docId1);
            Lock lock2 = lockManager.getLock(docId2);
            lock2.lock();
            try {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                lock1.lock();
                try {
                    updateDocument(docId2, content2);
                } finally {
                    lock1.unlock();
                }
            } finally {
                lock2.unlock();
            }
        };

        Thread t1 = new Thread(task1, "Deadlock-Thread-1");
        Thread t2 = new Thread(task2, "Deadlock-Thread-2");
        t1.start();
        t2.start();
        t1.join();

    }

}