package me.melon.rpc;

/**
 * @author melon.zhao
 * @since 2019/2/14
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(String name) {
        return "say hello:"+name;
    }
}
