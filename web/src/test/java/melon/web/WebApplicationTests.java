package melon.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

    @Test
    public void contextLoads() {
        String str = "呜呜#搜索#肯定";
        String str1 = "呜呜#搜索#肯定";
        String str2 = "##";
        String str3 = "null#null#";
        List<String> split = Arrays.asList(str.split("#"));
        List<String> split1 = Arrays.asList(str1.split("#"));
        List<String> split2 = Arrays.asList(str2.split("#"));
        List<String> split3 = Arrays.asList(str3.split("#"));
        System.out.println(split);
        System.out.println(split1);
        System.out.println(split2.size());

        StringBuilder sb = new StringBuilder("asks");
        System.out.println(sb.delete(0,sb.length()).toString());
        sb.append("pp");
        System.out.println(sb.toString());
    }

    @Test
    public void testList() {
        List list = Arrays.asList("a","b","c","d","e");
        list.removeAll(Arrays.asList("b","g","a"));
        System.out.println(list);
    }


}
