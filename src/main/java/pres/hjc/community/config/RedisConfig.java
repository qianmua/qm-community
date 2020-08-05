package pres.hjc.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  21:32
 * @description :
 */
@Configuration
public class RedisConfig {

    /**
     *
     * @param factory redis连接工厂 练级数据库
     * @return templates
     */
    @Bean
    @Primary
    public RedisTemplate<String , Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 传入 连接 工厂
        template.setConnectionFactory(factory);

        // 设置key 序列化
        // 序列化字符串
        template.setKeySerializer(RedisSerializer.string());
        // 设置Value 序列化
        template.setValueSerializer(RedisSerializer.json());

        //hash Key序列化
        template.setHashKeySerializer(RedisSerializer.string());
        //hash value序列化
        template.setHashValueSerializer(RedisSerializer.json());

        //reload
        template.afterPropertiesSet();

        return template;

    }
}
