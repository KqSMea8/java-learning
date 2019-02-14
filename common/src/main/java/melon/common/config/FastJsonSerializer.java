package melon.common.config;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @user: melon.zhao
 * @date: 2018/8/21
 */
public class FastJsonSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJsonSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }


    @Override
    public byte[] serialize(T o) throws SerializationException {
        if ( o == null ) {
            return new byte[0];
        }
        return JSON.toJSONString(o).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes,DEFAULT_CHARSET);
        System.out.println("---->deserialize:"+str);
        return (T) JSON.parseObject(str,clazz);
    }
}
