package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import pres.hjc.community.service.FollowService;
import pres.hjc.community.tools.GenRedisKeyUtil;

import java.util.List;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/6  12:17
 * @description :
 */
@Service
@Primary
public class FollowServiceImpl implements FollowService {

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = GenRedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                //关注我
                operations.opsForZSet().add(followeeKey , entityId , System.currentTimeMillis());
                //我关注
                operations.opsForZSet().add(followerKey , userId , System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    @Override
    public void unFollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = GenRedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                //关注我
                operations.opsForZSet().remove(followeeKey , entityId);
                //我关注
                operations.opsForZSet().remove(followerKey , userId );

                return operations.exec();
            }
        });
    }

    @Override
    public long findFolloweeCount(int userId, int entityType) {
        String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, entityType);

        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    @Override
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = GenRedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    @Override
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, entityType);
        Double score = redisTemplate.opsForZSet().score(followeeKey, entityId);

        return score != null;
    }

    @Override
    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit) {
        return null;
    }
}
