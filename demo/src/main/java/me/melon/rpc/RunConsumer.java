package me.melon.rpc;

/**
 * @author melon.zhao
 * @since 2019/2/14
 */
public class RunConsumer {

    public static void main(String[] args) {
        HelloService refer = RpcTaste.refer(HelloService.class, "127.0.0.1", 1234);
        for (int i = 0; i < 100; i++) {
            String say = refer.say(i + "");
            System.out.println(say);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
