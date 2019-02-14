package melon.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import melon.web.service.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Api(description = "Redis-MQ")
@RestController
@RequestMapping("/mq")
public class MQController {

    @Autowired
    Publisher publisher;

    @ApiOperation(value = "发布消息",notes = "notes 测试")
    @ApiImplicitParam(name = "msg",required = true,dataType = "String")
    @GetMapping
    public String publish(@RequestParam String msg){
        publisher.publish(msg);
        return msg;
    }
}
