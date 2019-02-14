package melon.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class Publisher implements MessagePublisher {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private ChannelTopic topic;

    public Publisher(){}

    public Publisher(ChannelTopic topic ) {
        this.topic = topic;
    }

    @Override
    public void publish(String msg) {
        redisTemplate.convertAndSend(topic.getTopic(),msg);
    }
}
