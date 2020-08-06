package pres.hjc.community.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import pres.hjc.community.service.FollowService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.GenRedisKeyUtil;

import java.util.*;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/6  12:17
 * @description :
 */
@Service
@Primary
public class FollowServiceImpl implements FollowService, CommunityRegisterStatus {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;


    @Override
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = GenRedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                //关注我
                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                //我关注
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

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
                operations.opsForZSet().remove(followeeKey, entityId);
                //我关注
                operations.opsForZSet().remove(followerKey, userId);

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
        String followeeKey = GenRedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);

        return getFollowInfo(followeeKey , offset , limit);
    }

    @Override
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit) {
        String followerKey = GenRedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER , userId);
        return getFollowInfo(followerKey , offset , limit);
    }

    /**
     * 获取 关注 用户 的 细心
     * 关注列表
     * @param followKey followKey
     * @param offset offset
     * @param limit limit
     * @return follow
     */
    private List<Map<String , Object>> getFollowInfo(String followKey , int offset, int limit){

        // 小到大 《- 时间
        // 最新
        // 倒叙
        Set<Integer> set =  redisTemplate.opsForZSet()
                .reverseRange(followKey, offset, offset + limit - 1);
        // Set userId

        if (set.isEmpty()){
            return null;
        }
        val maps = new ArrayList<Map<String, Object>>();
        set.forEach(v1 -> {
            val hashMap = new HashMap<String, Object>();
            val userPO = userService.selectById(v1);
            hashMap.put("user" , userPO);
            //关注 时间
            Double score = redisTemplate.opsForZSet().score(followKey, v1);
            hashMap.put("followTime", new Date(score.longValue()));

            maps.add(hashMap);
        });
        return maps;
    }
}
