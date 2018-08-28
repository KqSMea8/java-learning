package melon.jsoup.utils;

import io.netty.util.concurrent.RejectedExecutionHandlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @user: melon.zhao
 * @date: 2018/8/27
 */
public class ThreadUtils {
    public static ExecutorService newCachedThreadPool(){
        return new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L,
                TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
    }

}
