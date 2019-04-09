package melon.web.controller;

import com.alibaba.fastjson.JSON;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import melon.web.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MovieController {
//    @Resource
//    MovieService movieService;
//    @GetMapping
//    public String doFetch(){
//        log.info("start fetch...");
//        movieService.startFetch();
//        return "do fetch";
//    }
    @PostMapping
    public String test(@RequestBody List<String> ids,@RequestBody String[] idss) {
        System.out.println(JSON.toJSONString(ids));
        System.out.println(idss);
        return JSON.toJSONString(ids) + "-->" + idss;
    }
}
