package melon.web.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
public class Subscriber implements MessageListener {


    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.format("receive from topic[%s],msg:[%s]",new String(message.getChannel(),Charset.forName("utf-8")),
                new String(message.getBody(),Charset.forName("utf-8")));
    }
}
