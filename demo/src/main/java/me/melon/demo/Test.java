package me.melon.demo;

/**
 * @author Otis.z
 * @date 2019-04-04
 */
public class Test {
    static void pong(){
        System.out.print("pong");
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(()-> pong()).start();
        Thread.sleep(1);
        System.out.printf("ping");
    }
}
