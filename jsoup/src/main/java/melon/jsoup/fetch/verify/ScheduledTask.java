package melon.jsoup.fetch.verify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import melon.jsoup.domain.DoubanMovieItem;
import melon.jsoup.fetch.page.DoubanFetchResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * @user: melon.zhao
 * @date: 2018/8/24
 */

@Component
public class ScheduledTask {
    @Autowired
    VerifyIP verifyIP;
    @Autowired
    DoubanFetchResolver resolver;

    @Scheduled(cron = "0 */20 * * * *")
    public void start(){
//        verifyIP.verifyFetchUrl();
//        verifyIP.verifyCacheUrl();
        List<DoubanMovieItem> movieList = resolver.getMovieList(0);
    }

    public static void main(String[] args) {

    }


}
