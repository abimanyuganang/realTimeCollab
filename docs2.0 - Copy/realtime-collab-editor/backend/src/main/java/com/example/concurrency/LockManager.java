package com.example.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private final ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    public Lock getLock(String docId) {
        return lockMap.computeIfAbsent(docId, k -> new ReentrantLock());
    }
}