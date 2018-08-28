package melon.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "melon.mybatis.mapper")
@SpringBootApplication
public class MybatisApplication {

    public static void main(String[] args) {
        System.out.println("hello world~ classpath:"+MybatisApplication.class.getResource("/").toString());
        SpringApplication.run(MybatisApplication.class, args);
    }
}
