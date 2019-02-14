package me.melon.demo;

import sun.awt.windows.ThemeReader;

/**
 * @author melon.zhao
 * @since 2019/2/13
 */
public class DeadLock {
    public static String A = "A";
    public static String B = "B";

    public static void main(String[] args) {
        new DeadLock().deadLock();
    }
    private void deadLock() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A) {
                    System.out.println(Thread.currentThread().getName() + " get A...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " wake...");
                    synchronized (B) {
                        System.out.println(Thread.currentThread().getName() + " get B");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    System.out.println(Thread.currentThread().getName() + " get B...");
                    synchronized (A) {
                        System.out.println(Thread.currentThread().getName() + " get A");
                    }
                }
            }
        });

        t1.start();
        t2.start();

    }
}
