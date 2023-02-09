package ru.otus.executors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberSequence {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicInteger monitor = new AtomicInteger(0);

    private boolean raiseFlag = true;


    public static void main(String[] args) {
        new NumberSequence().go();
    }

    public void go() {

        Thread thread1 = new Thread(() -> firstTask(), "first thread");
        Thread thread2 = new Thread(() -> secondTask(), "second thread");

        thread1.start();
        thread2.start();
    }

    private void secondTask() {
        try {
            latch.await();
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    System.out.println(Thread.currentThread().getName() + ": " + monitor.get());
                    sleep();
                    monitor.notify();
                    monitor.wait();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void firstTask() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    if (monitor.get() == 0) {
                        latch.countDown();
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
                    monitor.notify();
                    monitor.wait();
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
