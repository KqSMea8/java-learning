package melon.jsoup.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import melon.jsoup.domain.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @user: melon.zhao
 * @date: 2018/8/22
 */
@Repository("redisManager")
@Slf4j
public class IPRedisManager {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public int total() {
        return redisTemplate.keys("*").size();
    }

    public void save(IP ip) {
        log.info("IPRedisManager.save---->ip:{}", JSON.toJSONString(ip));
        redisTemplate.opsForValue().set(getKey(ip),ip);
    }

    public void batchSave(List<List<IP>> ips) {
        log.info("IPRedisManager.batchSave---->ip list:{}",JSON.toJSONString(ips));
        for (List<IP> list : ips) {
            list.forEach(this::save);
        }
    }

    public IP getByKey(String key) {
        log.info("IPRedisManager.getByKey---->key:{}",key);
        return JSON.parseObject(redisTemplate.opsForValue().get(key).toString(),IP.class);
    }

    public IP getRandomly() {
        String key = redisTemplate.randomKey();
        return getByKey(key);
    }

    public List<IP> getAll(){
        Set<String> keys = redisTemplate.keys("*");
        List<IP> res = new ArrayList<>();
        keys.forEach(key -> {
            res.add(getByKey(key));
        });
        return res;
    }


    private String getKey(IP ip) {
        return new StringBuilder(ip.getIp()).append(":").append(ip.getPort()).toString();
    }

    public void deleteByKey(String key) {
        log.info("IPRedisManager.deleteByKey---->key:{}",key);
        if (StringUtils.isEmpty(key)) return;
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void delete(IP ip) {
        deleteByKey(getKey(ip));
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        keys.forEach(key -> {
            redisTemplate.opsForValue().getOperations().delete(key);
        });
    }


}
