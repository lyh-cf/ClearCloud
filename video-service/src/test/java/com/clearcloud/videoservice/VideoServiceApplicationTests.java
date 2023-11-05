package com.clearcloud.videoservice;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class VideoServiceApplicationTests {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Test
    void testZset() {
        for(int i=1;i<=5;i++) redisTemplate.opsForZSet().add("zset",i,i);
        //[5, 4, 3, 2]
        Set<Object> zset = redisTemplate.opsForZSet().reverseRange("zset", 0, 3);
        for (Object obj : zset) {
            if (obj instanceof Integer) {
                System.out.println("Integer");
            }
            else if(obj instanceof String){
                System.out.println("String");
            }
        }
//        for(int i:zset){
//            System.out.println(i);
//        }
        System.out.println(zset);
    }
    @Test
    void testRedisBloom(){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("ClearCloud:BloomFilter");
        //初始化，容量1000，错误率千分之三
        bloomFilter.tryInit(100000, 0.001);
        //add1000个
        for (int i = 0; i < 1000; i++) {
            bloomFilter.add("tom " + i);
        }
        int count = 0;
        //查询1000次
        for (int i = 0; i < 1000; i++) {
            if (bloomFilter.contains("bob" + i)) {
                count++;
            }
        }
        System.out.println("错误个数=" + count);
        System.out.println("预计插入数量：" + bloomFilter.getExpectedInsertions());
        System.out.println("容错率：" + bloomFilter.getFalseProbability());
        System.out.println("hash函数的个数：" + bloomFilter.getHashIterations());
        System.out.println("插入对象的个数：" + bloomFilter.count());
    }
    @Test
    void testBatchGet(){
        //这里获取String类型的序列化器
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        //第二个参数是指定结果反序列化器，用于反序列化管道中读到的数据，不是必传，
        //如果不传，则使用自定义RedisTemplate的配置，
        //如果没有自定义，则使用RedisTemplate默认的配置（JDK反序列化）
        List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(@NotNull RedisConnection connection) throws DataAccessException {
                for (int i = 1; i <= 10; i++) {
                    connection.get(("ClearCloud:"+i).getBytes());
                }
                //这里bytes只会获取到null，因为这里get操作只是放在管道里面，并没有
                //真正执行，所以获取不到值
                //byte[] bytes = connection.get("test:1".getBytes());

                //executePipelined 这个方法需要返回值为null，不然会抛异常，
                //这一点可以查看executePipelined源码
                return null;
            }
        }, stringSerializer);

        list.forEach(System.out::println);
    }
    @Test
    void testBatchSet(){
        Map<String, Object> data = new HashMap<>();
        for(int i=1;i<=10;i++){
            data.put("ClearCloud:"+i,i);
        }
        redisTemplate.executePipelined((RedisConnection connection) -> {
            data.forEach((key, value) -> {
                redisTemplate.opsForValue().set(key, value);
            });
            return null;
        });
    }
}
