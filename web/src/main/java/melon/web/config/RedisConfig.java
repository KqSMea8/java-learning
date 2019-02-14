package melon.web.config;

import melon.web.service.MessagePublisher;
import melon.web.service.Publisher;
import melon.web.service.Subscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    /**
     * 创建自定义序列化的redisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        FastJsonSerializer<Object> serializer = new FastJsonSerializer<>(Object.class);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public JedisConnectionFactory factory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new Subscriber());
    }
    @Bean
    public MessagePublisher messagePublisher() {
        return new Publisher(topic());
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(factory());
        redisMessageListenerContainer.addMessageListener(messageListener(),topic());
        return redisMessageListenerContainer;
    }


    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new Subscriber());
    }

    @Bean
    public ChannelTopic topic(){
        return new ChannelTopic("redis-mq");
    }
}
