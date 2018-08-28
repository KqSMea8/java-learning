package melon.jsoup.fetch.verify;




import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import melon.jsoup.dao.IPRedisManager;
import melon.jsoup.domain.IP;
import melon.jsoup.fetch.AbstractFetchUrl;
import melon.jsoup.fetch.ip.KDLFecthResolver;
import melon.jsoup.fetch.ip.XiciFetchResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;




/**
 * @user: melon.zhao
 * @date: 2018/8/22
 */
@Component
public class VerifyIP {
    private static Logger logger = LoggerFactory.getLogger(VerifyIP.class);
    private static String VERIFY_URL = "http://www.baidu.com";
    private static boolean running = true;
    public static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    public static BlockingQueue fectch = new LinkedBlockingQueue(10000);
    public static BlockingQueue cached = new LinkedBlockingQueue(10000);

    @Autowired
    private  IPRedisManager redisManager ;

    public  boolean verify(String ip,String port) {
        logger.info("VerifyIP.verify---->ip:{},port:{}",ip,port);
        boolean useful = false;
        HttpURLConnection  connection = null;

        try {
            URL url = new URL(VERIFY_URL);
            if (StringUtils.isEmpty(ip) && StringUtils.isEmpty(port)) return false;
            InetSocketAddress address = new InetSocketAddress(ip,Integer.valueOf(port));
            Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setConnectTimeout(4 * 1000);
            connection.setInstanceFollowRedirects(false);
            connection.setReadTimeout(6 * 1000);
            int res = connection.getResponseCode();
            logger.info("VerifyIP.verify---->res:{}",res);
            useful = res == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("verify proxy exception:{}",e.getMessage());
        }
        logger.info("verify proxy ip:{},port:{},useful:{}",ip,port,useful);
        return useful;
    }

    public void verify(List<IP> ips) {
        ips.forEach(item -> {
            boolean verify = verify(item.getIp(), item.getPort());
            if (verify) {
                redisManager.save(item);
            }else redisManager.delete(item);
        });
    }


    public  void verifyFetchUrl() {
        List<AbstractFetchUrl<List<IP>>> resolvers = Arrays.asList(
                new XiciFetchResolver(5, 1000),
                new KDLFecthResolver(5,1000)
        );
        resolvers.forEach(reslove -> {
            List<List<IP>> lists = reslove.fetchAll();
            if (CollectionUtils.isEmpty(lists)) return;
            lists.forEach(item -> {
                fectch.addAll(item);
            });

        });
        startVerify(fectch,"FETCH PROXY");
    }

    public  void verifyCacheUrl() {
        List<IP> all = redisManager.getAll();
        if (CollectionUtils.isEmpty(all)) return;
        cached.addAll(all);
        logger.info("redis中还有 {} 条ip",redisManager.total());
        startVerify(cached,"CACHED PROXY");
    }

    public  void startVerify(BlockingQueue<IP> queue,final String pName){
        Executors.newFixedThreadPool(1).execute(()->{
            while (running) {
                logger.info("{} 开始验证IP",pName);
                try {
                    IP ip = queue.take();
                    if (ip == null) continue;
                    if(verify(ip.getIp(),ip.getPort())){
                        ip.setCheckDate(new Date().getTime());
                        redisManager.save(ip);
                    }else redisManager.delete(ip);
                } catch (Exception e) {
                    logger.error("{} startVerify error",pName,e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


}
