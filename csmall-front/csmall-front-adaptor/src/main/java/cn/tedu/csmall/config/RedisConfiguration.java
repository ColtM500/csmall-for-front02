package cn.tedu.csmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {
    /**
     * 如果使用默认的redisTemplate 序列化 key值object value值object
     * hkey object hval object
     * value一般存放的是对象数据
     * @param redisConnectionFactory
     * @return
     * redisTemplate只保存和获取对象数据
     * 如果要是处理string类型的计步器,hash 计步器 hincrby StringRedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        //泛型不使用serializable 使用object 保持和sso项目生成的用户redis数据一致
        //否则有可能出现反序列化出异常
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //使用java()序列化和反序列化,导致string相关的命令 计步器,不能用了
        redisTemplate.setValueSerializer(RedisSerializer.java());
        //项目中有可能用到hash结构,所以也要定义hash的序列化
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        return redisTemplate;
    }

}