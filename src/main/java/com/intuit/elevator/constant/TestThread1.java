package com.intuit.elevator.constant;

public class TestThread1 implements Runnable{
    private volatile int state=1;
    private Thread activeThread;

    public void start() {
        activeThread = new Thread(this);
        activeThread.start();
    }

    @Override
    public void run() {
        while(true){
            if(state==1){
                action();
            }else{
                System.out.println("resume");
                break;
            }
        }
    }

    public  void action(){
            try {
                System.out.println("Before Sleep");
                Thread.sleep(200000000);
            } catch (InterruptedException ex) {
                System.out.println("Here");
            }
    }

    public void attention(){
        activeThread.interrupt();
        state=2;
    }

    public static void main(String[] args) throws InterruptedException {
        TestThread1 testThread11 = new TestThread1();
        Thread t = new Thread(new TestThread2(testThread11));
        t.start();

        t.join();
    }
}