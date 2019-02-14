package melon.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import melon.web.repo.RedisRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("redis分布式锁测试")
@RestController
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    RedisRepo redisRepo;
    @ApiOperation("测试锁")
    @ApiImplicitParam(name = "times",required = true,value = "测试锁次数")
    @GetMapping("/lock/{times}")
    public void lock(@PathVariable("times") Integer times){

        redisRepo.setLockKey("redisLock");
        while (times-- > 0){
            new Thread(() -> {
                try {
                    if (redisRepo.tryLock()) {
                        logger.info("{},获得锁...时间:{}",Thread.currentThread().getName(),System.currentTimeMillis()/1000);
                        try {
                            logger.info("睡....");
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }else {
                        logger.info("{},未获得锁...时间:{}",Thread.currentThread().getName(),System.currentTimeMillis()/1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    redisRepo.unlock();
                }
            }).start();
        }
    }
}
