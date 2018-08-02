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
import tk.mybatis.spring.annotation.MapperScan;

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

}
