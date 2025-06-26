package com.example.concurrency;

import com.example.service.DocumentService;

public class ConcurrencyDemo {
    public static void main(String[] args) throws InterruptedException {
        DocumentService service = new DocumentService();
        service.createDocument("doc1", "Initial", 1);

        Thread t1 = new Thread(new DocumentUpdater(service, "doc1", "Content from t1"));
        Thread t2 = new Thread(new DocumentUpdater(service, "doc1", "Content from t2"));

        t1.setPriority(Thread.MAX_PRIORITY); // Thread influencing
        t2.setPriority(Thread.MIN_PRIORITY);

        t1.start();
        t2.start();

        t1.join(); // Joining threads
        t2.join();

        System.out.println("Final content: " + service.getDocument("doc1").getContent());
    }
}