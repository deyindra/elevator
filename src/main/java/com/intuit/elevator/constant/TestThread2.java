package com.intuit.elevator.constant;

public class TestThread2 implements Runnable {
    private TestThread1 testThread1;

    public TestThread2(TestThread1 testThread1) {
        this.testThread1 = testThread1;
        this.testThread1.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            testThread1.attention();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
