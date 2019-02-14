package melon.web.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RedisRepo {
    private static final Logger logger = LoggerFactory.getLogger(RedisRepo.class);
    /**
     * 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
     */
    public static final String NX = "NX";

    /**
     * seconds — 以秒为单位设置 key 的过期时间，等效于EXPIRE key seconds
     */
    public static final String EX = "EX";

    //默认锁的有效时间
    private static final int EXPIRE_TIME = 30;
    //默认请求锁的超时时间 毫秒
    private static final long TIME_OUT = 100;

    private String lockKey;
    private String lockValue;
    private int expireTime = EXPIRE_TIME;
    private long time = TIME_OUT;

    private volatile boolean locked = false;

    private String lockKeyLog = "";

    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }



    @Autowired
    RedisTemplate redisTemplate;

    public String setNX(final String key,final String requestId,final long expire) {
        return (String) redisTemplate.execute((RedisCallback<String>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            String result = null;
            if (nativeConnection instanceof Jedis) {
                result = ((Jedis) nativeConnection).set(key,requestId,NX,EX,expire);
                String value = ((Jedis) nativeConnection).get(key);
                logger.info("result:{} key:{}  value:{}",result,key,value);
            }
            if (!StringUtils.isEmpty(lockKeyLog) && !StringUtils.isEmpty(result)) {
                logger.info("获取锁[{}]的时间:{}",lockKeyLog,System.currentTimeMillis());
            }
            return result;
        });
    }

    public boolean tryLock(){
        lockValue = UUID.randomUUID().toString().replaceAll("-","");
        long timeout = time ;
        long nowTime = System.currentTimeMillis();
        //允许的毫秒范围内尝试获取锁
        while((System.currentTimeMillis() - nowTime) < timeout) {
            if ("OK".equalsIgnoreCase(setNX(lockKey,lockValue,expireTime))){
                locked = true;
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //locked 是共享变量 有线程获得锁是就会为true 需要判断value是否为当前线程的requestId
        //如果是才说明获得了锁 不是则没有
        if (locked && !lockValue.equals(redisTemplate.opsForValue().get(lockKey))) return false;
        return locked;
    }


    public boolean unlock() {
        if (locked) {
            //locked 是共享变量 有线程获得锁是就会为true 需要判断value是否为当前线程的requestId
            //如果是才说明获得了锁 不是则没有
            if (!lockValue.equals(redisTemplate.opsForValue().get(lockKey))) return false;
            return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                logger.info("{},unlock...",Thread.currentThread().getName());
                Object nativeConnection = connection.getNativeConnection();
                Long result = 0L;

                List<String> keys = new ArrayList<>();
                keys.add(lockKey);
                List<String> values = new ArrayList<>();
                values.add(lockValue);

                if (nativeConnection instanceof Jedis) {
                    result = (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA,keys,values);
                }

                if (result == 0 && !StringUtils.isEmpty(lockKeyLog)) {
                    logger.info("Redis分布式锁,解锁 {} 失败| 解锁时间:{}",lockKeyLog,System.currentTimeMillis());
                }

                locked = result == 0;
                return result == 1;
            });
        }

        return true;
    }


    public String getLockKeyLog() {
        return lockKeyLog;
    }

    public void setLockKeyLog(String lockKeyLog) {
        this.lockKeyLog = lockKeyLog;
    }

    public String getLockKey() {

        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getLockValue() {
        return lockValue;
    }

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }



}
