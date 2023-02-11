package ru.otus.executors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberSequence {

    private final static String THREAD_1_NAME = "first thread";
    private final static String THREAD_2_NAME = "second thread";
    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicInteger monitor = new AtomicInteger(0);
    private String state = "";
    private boolean raiseFlag = true;


    public static void main(String[] args) {
        new NumberSequence().go();
    }

    public void go() {
        Thread thread1 = new Thread(() -> firstTask(), THREAD_1_NAME);
        Thread thread2 = new Thread(() -> secondTask(), THREAD_2_NAME);

        thread1.start();
        thread2.start();
    }

    private void secondTask() {
        try {
            latch.await();
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {

                    while (!Thread.currentThread().getName().equals(state)) {
                        monitor.wait();
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + monitor.get());
                    sleep();
                    state = THREAD_1_NAME;
                    monitor.notify();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void firstTask() {
        try {
            Thread.sleep(5000);
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    if (monitor.get() == 0) {
                        state = THREAD_1_NAME;
                        latch.countDown();
                    }

                    while (!Thread.currentThread().getName().equals(state)) {
                        monitor.wait();
                    }

                    if (raiseFlag) {
                        System.out.println(Thread.currentThread().getName() + ": " + monitor.incrementAndGet());
                    } else {
                        System.out.println(Thread.currentThread().getName() + ": " + monitor.decrementAndGet());
                    }

                    if ((raiseFlag && monitor.get() + 1 == 11)
                            || (!raiseFlag && monitor.get() - 1 == 0)) {
                        raiseFlag = !raiseFlag;
                    }

                    sleep();

                    state = THREAD_2_NAME;
                    monitor.notify();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
