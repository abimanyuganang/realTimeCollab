package com.example.concurrency;

import java.util.Arrays;
import java.util.List;

public class ParallelStreamDemo {
    public static void main(String[] args) {
        List<String> docs = Arrays.asList("doc1", "doc2", "doc3", "doc4");
        long count = docs.parallelStream()
            .filter(doc -> doc.startsWith("doc"))
            .count();
        System.out.println("Count: " + count);
    }
}