package melon.mybatis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import melon.mybatis.mapper.CustomerMapper;
import melon.mybatis.po.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisApplicationTests {

    @Autowired
    private CustomerMapper mapper;

    @Test
    public void contextLoads() {
        System.out.println("hello world~");
    }

    @Test
    public void testTkMapperSelect(){
        Customer customer = new Customer();
        customer.setId("rP61kDNx04");
        customer = mapper.selectOne(customer);
        System.out.println(JSON.toJSONString(customer));
    }

    @Test
    public void testNullException(){
        List list = new ArrayList();
        list.forEach((item)->{
            System.out.println(item);
        });
    }

    @Test
    public void testTime(){
        System.out.println(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        System.out.println(calendar.getTimeInMillis());
        Calendar start = Calendar.getInstance();
        start.setTime(calendar.getTime());
        start.set(Calendar.HOUR_OF_DAY,0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        System.out.println(start.getTimeInMillis());
        System.out.println(calendar.getTimeInMillis()-start.getTimeInMillis());
    }



}
