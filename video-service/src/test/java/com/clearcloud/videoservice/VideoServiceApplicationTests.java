package com.clearcloud.videoservice;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Set;

@SpringBootTest
class VideoServiceApplicationTests {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Test
    void testZset() {
        for(int i=1;i<=5;i++) redisTemplate.opsForZSet().add("zset",i,i);
        //[5, 4, 3, 2]
        Set<Integer> zset = redisTemplate.opsForZSet().reverseRange("zset", 0, 3);
//        for (Object obj : zset) {
//            if (obj instanceof Integer) {
//                System.out.println("Integer");
//            }
//            else if(obj instanceof String){
//                System.out.println("String");
//            }
//        }
        for(int i:zset){
            System.out.println(i);
        }
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
}
