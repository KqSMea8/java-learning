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
            System.out.println("schedule task execute..."+new Date().getTime()/1000);
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
        String res = "{\"data\":[{\"directors\":[\"詹姆斯·霍尼伯内\"],\"rate\":\"9.9\",\"cover_x\":1530,\"star\":\"50\",\"title\":\"蓝色星球2\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26979545\\/\",\"casts\":[\"戴维·阿滕伯勒\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2518413249.jpg\",\"id\":\"26979545\",\"cover_y\":2176},{\"directors\":[\"伊丽莎白·怀特\",\"贾斯汀·安德森\",\"艾玛·纳珀\",\"艾德·查尔斯\",\"查登·亨特\",\"弗雷迪·德瓦斯\"],\"rate\":\"9.9\",\"cover_x\":1600,\"star\":\"50\",\"title\":\"地球脉动 第二季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26733371\\/\",\"casts\":[\"戴维·阿滕伯勒\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2410512421.jpg\",\"id\":\"26733371\",\"cover_y\":2400},{\"directors\":[\"贾斯汀·罗兰\",\"Juan Jose Meza-Leon\"],\"rate\":\"9.8\",\"cover_x\":440,\"star\":\"50\",\"title\":\"瑞克和莫蒂 第三季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26592971\\/\",\"casts\":[\"贾斯汀·罗兰\",\"克里斯·帕内尔\",\"斯宾瑟·格拉默\",\"萨拉·乔克\",\"内森·菲利安\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2453814101.jpg\",\"id\":\"26592971\",\"cover_y\":640},{\"directors\":[\"韦斯利·阿彻\",\"皮特·米歇尔斯\"],\"rate\":\"9.8\",\"cover_x\":773,\"star\":\"50\",\"title\":\"瑞克和莫蒂 第二季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/25865470\\/\",\"casts\":[\"贾斯汀·罗兰\",\"克里斯·帕内尔\",\"斯宾瑟·格拉默\",\"萨拉·乔克\",\"科甘-迈克尔·凯\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2495174549.jpg\",\"id\":\"25865470\",\"cover_y\":1000},{\"directors\":[\"Kevin Bright\"],\"rate\":\"9.8\",\"cover_x\":1170,\"star\":\"50\",\"title\":\"老友记 第十季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286552\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2187822907.jpg\",\"id\":\"3286552\",\"cover_y\":1600},{\"directors\":[\"Sydney Lotterby\",\"彼得·惠特莫尔\"],\"rate\":\"9.8\",\"cover_x\":420,\"star\":\"50\",\"title\":\"是，大臣  第三季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/4933235\\/\",\"casts\":[\"保罗·爱丁顿\",\"奈杰尔·霍桑\",\"德里克·福德斯\",\"伊莲诺·布罗\",\"约翰·内特尔顿\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2187837418.jpg\",\"id\":\"4933235\",\"cover_y\":599},{\"directors\":[\"彼得·惠特莫尔\",\"Sydney Lotterby\"],\"rate\":\"9.8\",\"cover_x\":840,\"star\":\"50\",\"title\":\"是，大臣  第二季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/4933194\\/\",\"casts\":[\"保罗·爱丁顿\",\"奈杰尔·霍桑\",\"德里克·福德斯\",\"戴安娜·霍迪诺特\",\"约翰·内特尔顿\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2187837276.jpg\",\"id\":\"4933194\",\"cover_y\":1228},{\"directors\":[\"Sydney Lotterby\"],\"rate\":\"9.8\",\"cover_x\":849,\"star\":\"50\",\"title\":\"是，首相  第二季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/5359940\\/\",\"casts\":[\"保罗·爱丁顿\",\"奈杰尔·霍桑\",\"德里克·福德斯\",\"戴安娜·霍迪诺特\",\"黛柏拉·诺顿\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2187836590.jpg\",\"id\":\"5359940\",\"cover_y\":1195},{\"directors\":[\"罗布·沙利文\"],\"rate\":\"9.8\",\"cover_x\":600,\"star\":\"50\",\"title\":\"猎捕\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26576692\\/\",\"casts\":[\"David Attenborough\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2286709631.jpg\",\"id\":\"26576692\",\"cover_y\":874},{\"directors\":[\"彼得·惠特莫尔\"],\"rate\":\"9.8\",\"cover_x\":1071,\"star\":\"50\",\"title\":\"是，大臣 1984圣诞特辑\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26725031\\/\",\"casts\":[\"保罗·爱丁顿\",\"奈杰尔·霍桑\",\"德里克·福德斯\",\"戴安娜·霍迪诺特\",\"约翰·内特尔顿\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2397258764.jpg\",\"id\":\"26725031\",\"cover_y\":1500},{\"directors\":[\"Laurieann Gibson\"],\"rate\":\"9.9\",\"cover_x\":720,\"star\":\"50\",\"title\":\"Lady Gaga 恶魔舞会巡演之麦迪逊公园广场演唱会\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/6075345\\/\",\"casts\":[\"Lady Gaga\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2500154013.jpg\",\"id\":\"6075345\",\"cover_y\":1000},{\"directors\":[\"明基·斯皮罗\"],\"rate\":\"9.7\",\"cover_x\":2025,\"star\":\"50\",\"title\":\"风骚律师 第四季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/27077785\\/\",\"casts\":[\"鲍勃·奥登科克\",\"乔纳森·班克斯\",\"蕾亚·塞洪\",\"帕特里克·法比安\",\"迈克尔·曼多\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2526785183.jpg\",\"id\":\"27077785\",\"cover_y\":3000},{\"directors\":[\"张黎\"],\"rate\":\"9.7\",\"cover_x\":500,\"star\":\"50\",\"title\":\"大明王朝1566\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/2210001\\/\",\"casts\":[\"陈宝国\",\"黄志忠\",\"倪大红\",\"王庆祥\",\"郭东文\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2444453078.jpg\",\"id\":\"2210001\",\"cover_y\":700},{\"directors\":[\"何苦\"],\"rate\":\"9.7\",\"cover_x\":750,\"star\":\"50\",\"title\":\"最后的棒棒\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/26636841\\/\",\"casts\":[\"何苦\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2461128191.jpg\",\"id\":\"26636841\",\"cover_y\":1334},{\"directors\":[\"贾斯汀·罗兰\"],\"rate\":\"9.7\",\"cover_x\":564,\"star\":\"50\",\"title\":\"瑞克和莫蒂 第一季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/11537954\\/\",\"casts\":[\"贾斯汀·罗兰\",\"汤姆·肯尼\",\"克里斯·帕内尔\",\"斯宾瑟·格拉默\",\"萨拉·乔克\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2168567316.jpg\",\"id\":\"11537954\",\"cover_y\":810},{\"directors\":[\"Gail Mancuso\"],\"rate\":\"9.7\",\"cover_x\":783,\"star\":\"50\",\"title\":\"老友记  第三季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286531\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2195389285.jpg\",\"id\":\"3286531\",\"cover_y\":1024},{\"directors\":[\"Kevin Bright\"],\"rate\":\"9.7\",\"cover_x\":1089,\"star\":\"50\",\"title\":\"老友记  第五季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286538\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2330977709.jpg\",\"id\":\"3286538\",\"cover_y\":1500},{\"directors\":[\"Kevin Bright\"],\"rate\":\"9.7\",\"cover_x\":454,\"star\":\"50\",\"title\":\"老友记  第六季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286543\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img1.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2228920659.jpg\",\"id\":\"3286543\",\"cover_y\":640},{\"directors\":[\"Shelley Jensen\"],\"rate\":\"9.7\",\"cover_x\":1533,\"star\":\"50\",\"title\":\"老友记  第四季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286536\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2375152053.jpg\",\"id\":\"3286536\",\"cover_y\":2175},{\"directors\":[\"迈克尔·莱拜克\"],\"rate\":\"9.7\",\"cover_x\":984,\"star\":\"50\",\"title\":\"老友记 第二季\",\"url\":\"https:\\/\\/movie.douban.com\\/subject\\/3286528\\/\",\"casts\":[\"詹妮弗·安妮斯顿\",\"柯特妮·考克斯\",\"丽莎·库卓\",\"马特·勒布朗\",\"马修·派瑞\"],\"cover\":\"https://img3.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2200410776.jpg\",\"id\":\"3286528\",\"cover_y\":1400}]}";
        JSONArray data = JSON.parseObject(res).getJSONArray("data");
        System.out.println();
    }


}
