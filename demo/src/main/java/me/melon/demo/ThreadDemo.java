package me.melon.demo;

/**
 * @author melon.zhao
 * @since 2019/1/30
 */
public class ThreadDemo {

    private static Object object = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName()+" enter...");
                for (int i = 1; i <= 10; i++) {
                    System.out.println("iofkf");
                    if (100 % 2 == 0) {
                        System.out.println(i);

                        object.notify();
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName()+" enter...");
                for (int i = 1; i <= 10; i++) {
                    System.out.println("sksksksk");
                    if (100 % 2 == 1) {
                        System.out.println(i);

                        object.notify();
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

}
