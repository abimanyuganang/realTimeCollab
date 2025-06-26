package com.example.concurrency;

public class DeadlockDemo {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void methodA() {
        synchronized (lock1) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock2) {
                System.out.println("methodA acquired both locks");
            }
        }
    }

    public void methodB() {
        synchronized (lock2) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock1) {
                System.out.println("methodB acquired both locks");
            }
        }
    }

    public static void main(String[] args) {
        DeadlockDemo demo = new DeadlockDemo();
        Thread t1 = new Thread(demo::methodA);
        Thread t2 = new Thread(demo::methodB);
        t1.start();
        t2.start();
    }
}