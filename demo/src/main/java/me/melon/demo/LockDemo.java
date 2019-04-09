package me.melon.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Otis.z
 * @date 2019/3/25
 */
public class LockDemo implements Cloneable {

    public String test;
    private static final ReentrantLock lock = new ReentrantLock();

    public void business() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到锁");
            System.out.println("已获取锁的次数:" + lock.getHoldCount());
            TimeUnit.SECONDS.sleep(1);
            System.out.println("等待的队列:" + lock.getQueueLength());
            System.out.println(lock.isHeldByCurrentThread());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void reentrant() {
        for (int i = 0; i < 5; i++) {
            business();
        }
    }


    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        LockDemo lockDemo = new LockDemo();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> lockDemo.business()).start();
        }

//        Map<String,List<String>> map = new HashMap<>();
//        for (int i = 0; i < 10; i++) {
//            map.computeIfAbsent("a",k-> new ArrayList<>()).add(i+"");
//        }Ma
//        System.out.println(map.get("a"));

//        LockDemo lockDemo = new LockDemo();
//        lockDemo.test = "test";
//        LockDemo clone = (LockDemo) lockDemo.clone();
//        System.out.println( clone.equals(lockDemo));
//        System.out.println(clone.toString() + "---->lockDemo:" + lockDemo.toString());
//        System.out.println(clone == lockDemo);
//        System.out.println(clone.test == lockDemo.test );
//        System.out.println("clone test:"+clone.test);
//        System.out.println("lockDemo test:"+lockDemo.test);
//        lockDemo.test = "testCopy";
//        System.out.println("clone test:"+clone.test);
//        System.out.println("lockDemo test:"+lockDemo.test);
//        System.out.println( clone.equals(lockDemo));
//        Integer a = null;
//        System.out.println(a);
          System.in.read();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}

