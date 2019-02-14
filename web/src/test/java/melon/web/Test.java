package melon.web;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    AtomicInteger atomicInteger = new AtomicInteger(0);
    Integer integer = new Integer(0);

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/melon.zhao/Documents/out3.pdf");
        file.createNewFile();
        try (OutputStream os = new FileOutputStream(file)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withUri("file:///Users/melon.zhao/Desktop/spring.html");
            builder.withUri("file:///Users/melon.zhao/Desktop/test轻轻课程协议书V4的副本01.html");
            builder.toStream(os);
            builder.run();
        }
    }


    private void testThreadState() {
        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
    }

    static class TimeWaiting implements Runnable {

        @Override
        public void run() {
            while (true) {
                second(1000);
            }
        }
    }

    static class Waiting implements Runnable {

        @Override
        public void run() {
            synchronized (Waiting.class) {
                try {
                    Waiting.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Blocked implements Runnable {

        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    second(1000);
                }
            }
        }
    }

    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println(threadInfo.getThreadId() + "....." + threadInfo.getThreadName());
        }
    }

    private void testAtomicCount() {
        List<Thread> ts = new ArrayList<>(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    count();
                    safeCount();
                }
            });
            ts.add(t);
        }
        ts.forEach(item -> item.start());
        ts.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("atomicInteger:" + atomicInteger.get());
        System.out.println("integer:" + integer);
        System.out.println("duration:" + (System.currentTimeMillis() - start));
    }

    private void safeCount() {
        while (true) {
            int i = atomicInteger.get();
            boolean b = atomicInteger.compareAndSet(i, ++i);
            if (b) {
                break;
            }

        }
    }

    private void count() {
        integer++;
    }
}
