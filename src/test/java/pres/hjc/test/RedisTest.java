package pres.hjc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pres.hjc.community.CommunityBootstrapApplication;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  21:37
 * @description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityBootstrapApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void getAndSetTest(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey , 1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void setHashTest(){
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey , "id" , 1);
        redisTemplate.opsForHash().put(redisKey , "userName" , "qianmuna");

        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"userName"));

    }


    @Test
    public void setListsTest(){
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey , 101);
        redisTemplate.opsForList().leftPush(redisKey , 102);
        redisTemplate.opsForList().leftPush(redisKey , 103);

        //大小
        System.out.println(redisTemplate.opsForList().size(redisKey));
        // 索引位置数据
        System.out.println(redisTemplate.opsForList().index(redisKey , 2));
        System.out.println(redisTemplate.opsForList().range(redisKey ,0, 2));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));

    }

    @Test
    public void setSetTest(){
        String redisKey = "test:teacher";
        redisTemplate.opsForSet().add(redisKey , "A" , "B" , "C" , "D");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        // 随机弹
        System.out.println(redisTemplate.opsForSet().pop(redisKey));

    }


    @Test
    public void setDelKey(){
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));
        System.out.println(redisTemplate.expire("test:teacher", 10, TimeUnit.SECONDS));

    }


    @Test
    public void setBoundKeyTest(){
        // 多次访问同一个key
        // 使用 bound 绑定 //
        String redisKey = "test:user";
        BoundHashOperations operations = redisTemplate.boundHashOps(redisKey);

        Set keys = operations.keys();

        System.out.println(keys);
        System.out.println(operations.get("id"));
    }

    @Test
    public void setTransactional(){
        // 编程式 事务
        Object o = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";

                // 启用事务
                operations.multi();

                operations.opsForSet().add(redisKey , "A" , "E" , "F");
                operations.opsForSet().add(redisKey , "B");
                operations.opsForSet().add(redisKey , "C");
                operations.opsForSet().add(redisKey , "D");

                // 此时 并未提交
                // 所以 查不到的啦~

                //提交事务
                operations.exec();

                return null;
            }
        });
        System.out.println(o);
    }
}
