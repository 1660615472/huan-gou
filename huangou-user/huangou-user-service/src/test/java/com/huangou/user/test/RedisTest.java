package com.huangou.user.test;

import com.huangou.user.HuangouUserApplication;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RunWith (SpringRunner.class)
@SpringBootTest(classes = HuangouUserApplication.class) //测试用例
public class RedisTest {
    @Autowired //StringRedisTemplate提供序列化
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        //向redis存储数据
        this.stringRedisTemplate.opsForValue ().set ( "44444","valu2323e1" );
        //获取数据
        String key = this.stringRedisTemplate.opsForValue ().get ( "44444" ).toString ();
        System.out.println("vale="+key );
    }

    @Test
    public void testRedis2(){
        //设置redis存储时间
        this.stringRedisTemplate.opsForValue ().set ( "key2","value",5, TimeUnit.MINUTES );
    }

    @Test
    public void testHash(){
        BoundHashOperations<String,Object,Object> hashOps =
                this.stringRedisTemplate.boundHashOps ( "user" );
        //操作hash数据
        hashOps.put ( "name","jack" );
        hashOps.put("age","21");

        //获取单个数据
       Object name = hashOps.get("name");
        Object age = hashOps.get ( "age" );
        System.out.println ( name+"__"+age );

        //获取所有数据 遍历map
        Map<Object,Object> map =hashOps.entries ();
        for(Map.Entry<Object,Object> me : map.entrySet ()){
            System.out.println ( me.getKey ()+" : "+me.getValue () );
        }
    }
}
