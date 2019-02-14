package me.melon.demo;

import java.text.MessageFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author melon.zhao
 * @since 2019/1/24
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        //5个线程一组阻塞
//        CyclicBarrier barrier = new CyclicBarrier(5);
        //5个线程一组阻塞并完成其他逻辑
        CyclicBarrier barrier = new CyclicBarrier(5,()-> System.out.println("run barrier logic..."));
        for (int i = 0; i < 5; i++) {

            new WorkerThread(barrier).start();
        }
    }

    static class WorkerThread extends Thread{

        private CyclicBarrier barrier;
        WorkerThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+" enter,start sleep...");
                Thread.sleep(2000);
                long id = Thread.currentThread().getId();
                if (id == 14){
                    currentThread().interrupt();
                }
                int await = barrier.await();
                if (await % 2 == 0) {
                    System.out.println("thread 2/4...");
                }
                System.out.println(Thread.currentThread().getName()+" barrier end...run final work");
            } catch (InterruptedException e) {
                System.out.println(MessageFormat.format("barrier.broken:{0}",barrier.isBroken()));
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                System.out.println(MessageFormat.format("barrier.broken:{0}",barrier.isBroken()));
                e.printStackTrace();
            } finally {
                System.out.println("do final logic...");
            }
        }
    }

}
