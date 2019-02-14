package me.melon.rpc;

import java.io.IOException;

/**
 * @author melon.zhao
 * @since 2019/2/14
 */
public class RunProvider {

    public static void main(String[] args) throws IOException {
        HelloService helloService = new HelloServiceImpl();
        RpcTaste.export(helloService,1234);
    }
}
