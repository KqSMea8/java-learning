package melon.jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import melon.jsoup.dao.IPRedisManager;
import melon.jsoup.domain.DoubanMovieItem;
import melon.jsoup.domain.IP;
import melon.jsoup.fetch.ip.KDLFecthResolver;
import melon.jsoup.fetch.ip.XiciFetchResolver;
import melon.jsoup.fetch.page.DoubanFetchResolver;
import melon.jsoup.fetch.verify.VerifyIP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupApplicationTests {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    IPRedisManager manager;
    @Autowired
    VerifyIP verifyIP;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testJsoup(){
        String url = "http://www.xicidaili.com/nn";
        try {
            Document document = Jsoup.connect(url)
                    .timeout(3000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .get();
            System.out.println(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testParseHtml(){
        verifyIP.verifyFetchUrl();
        while(true);
    }

    @Test
    public void testPython() {
        String [] command = new String[] {"python3","./script/parse_font.py","e456ac1d3aa60ebf22b5c0b8b1376a152080.woff"};
        try {
            Process exec = Runtime.getRuntime().exec("python3 ./script/parse_font.py e456ac1d3aa60ebf22b5c0b8b1376a152080.woff");
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = null;
            while ( (line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            int re = exec.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXici() {
        XiciFetchResolver resolver = new XiciFetchResolver();
        List list = resolver.fetchAll();
    }

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("ip",new IP());
        Object ip = redisTemplate.opsForValue().get("ip");
        System.out.println(ip);
    }

    @Test
    public void testRedisSave(){
        KDLFecthResolver fetchUrl = new KDLFecthResolver(1,1000);
        List list = fetchUrl.fetchAll();
        manager.batchSave(list);
    }

    @Test
    public void testPattern () {
        Pattern pattern = Pattern.compile("[ _`~!@#$%^∑&*()+=|{}':;',\\[\\].·<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t");
        Matcher matcher = pattern.matcher("∑");
        boolean matches = matcher.find();
        System.out.println(matches);

    }

    @Test
    public void testRedisGet(){
        IP randomly = manager.getRandomly();
//        System.out.println(JSON.toJSONString(randomly)+"total:"+manager.total());
        IP ip = manager.getByKey("ip");
        System.out.println(ip);
    }

    @Test
    public void testRedisDelete() {
//        manager.deleteAll();
        manager.deleteByKey("skl");
    }



    @Test
    public void testVerifyIP(){
        IP randomly = manager.getRandomly();
        boolean verify = verifyIP.verify(randomly.getIp(), randomly.getPort());
    }


    @Test
    public void testScheduledExecutorService() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("task task execute..."+new Date().getTime()/1000);
            IP randomly = manager.getRandomly();
            boolean verify = verifyIP.verify(randomly.getIp(), randomly.getPort());
            if (!verify) manager.delete(randomly);
        },1000,1000,TimeUnit.MILLISECONDS);

        while (true);
    }

    @Test
    public void testScheduledTask(){
    }



    @Test
    public void testThread () {
        ExecutorService executorService = Executors.newFixedThreadPool(2,r -> new Thread(r, "thread-"+System.currentTimeMillis()/1000));
        for (int i = 0;i<5;i++){
            executorService.execute(()->{
                try {
                    System.out.println("sleep");
                    Thread.sleep(5000);
                    System.out.println("wake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数再加一倍，防止枚举时有可能刚好有动态线程生成
        int slackSize = topGroup.activeCount() * 2;
        Thread[] slackThreads = new Thread[slackSize];
        // 获取根线程组下的所有线程，返回的actualSize便是最终的线程数
        int actualSize = topGroup.enumerate(slackThreads);
        Thread[] atualThreads = new Thread[actualSize];
        // 复制slackThreads中有效的值到atualThreads
        System.arraycopy(slackThreads, 0, atualThreads, 0, actualSize);
        System.out.println("Threads size is " + atualThreads.length);
        for (Thread thread : atualThreads) {
            System.out.println("Thread name : " + thread.getName());
        }

        while (true);
    }


    @Autowired
    DoubanFetchResolver resolver;

    @Test
    public void testDouban () {
        int start = 0;
        while (true) {

            List<DoubanMovieItem> movieList = resolver.getMovieList(20*(start++));
            if (CollectionUtils.isEmpty(movieList)) break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("fetch total:"+start*20);
        }
    }

    @Test
    public void testParseDouban(){
    }

    @Test
    public void testPartten() {
        Pattern compile = Pattern.compile("\\d{11}");
        Matcher matcher = compile.matcher("18212341234");
        System.out.println(matcher.matches());

    }


}
