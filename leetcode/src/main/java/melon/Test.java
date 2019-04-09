package melon;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Otis.z
 * @date 2019-04-08
 */
public class Test extends Thread{
    private CountDownLatch countDownLatch;
    private CyclicBarrier cyclicBarrier;
    public Test(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public Test(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("wait:"+Thread.currentThread().getName());
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        try {
            cyclicBarrier.await();
            System.out.println("parties:"+cyclicBarrier.getParties() +"");
            System.out.println("waiting numbers:"+cyclicBarrier.getNumberWaiting() +"");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("release"+Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//
//        for (int i = 0; i < 5; i++) {
//            new Test(latch).start();
//        }
//        Thread.sleep(1000 * 5);
//        latch.countDown();
//        System.out.println("地方计算机");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        for (int i = 0; i < 6; i++) {
            new Test(cyclicBarrier).start();
        }
    }

}
