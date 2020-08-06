package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import pres.hjc.community.service.LikeService;
import pres.hjc.community.tools.GenRedisKeyUtil;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  22:16
 * @description :
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 点赞功能
     * 用户 点赞
     * 两次 更新
     * @param userId userId
     * @param entityType entityType
     * @param  entityId entityId
     * @param entityUserId entityUserId
     */
    @Override
    public void like(int userId, int entityType, int entityId, int entityUserId) {

        // redis 事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);
                // user Id
                String userLikeKey = GenRedisKeyUtil.getUserLikeKey(entityUserId);
                // 是否 点过赞？
                boolean member = operations.opsForSet().isMember(entityLikeKey, userId);

                // 请勿 在 redis 事务中 查询

                operations.multi();
                if (member){
                    // 取消
                    operations.opsForSet().remove(entityLikeKey , userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    //赞
                    redisTemplate.opsForSet().add(entityLikeKey , userId);
                    operations.opsForValue().increment(userLikeKey);

                }

                return operations.exec();
            }
        });
    }

    public void like(int userId, int entityType, int entityId) {
       like(userId, entityType, entityId , 0);
    }

    /**
     * 点赞数量
     * 对于 某一个 实体
     * @param entityType entityType
     * @param entityId entityId
     * @return
     */
    @Override
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);
        Long size = redisTemplate.opsForSet().size(entityLikeKey);
        return size == null ? 0: size ;
    }

    /**
     * user 的点赞状态//
     * 点？ 1
     * 无？ 0
     * @param userId userId
     * @param entityType  entityType
     * @param entityId entityId
     * @return
     */
    @Override
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);

        return redisTemplate.opsForSet().isMember(entityLikeKey , userId) ? 1 : 0;
    }

    /**
     * ===
     * @param userId userId
     * @return
     */
    @Override
    public int findUserLikeCount(int userId) {
        String entityLikeKey = GenRedisKeyUtil.getUserLikeKey(  userId);

        Integer o = (Integer) redisTemplate.opsForValue().get(entityLikeKey);


        return o == null ? 0 : o;
    }
}

/*
*

 //        String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);

        // 点赞
        // 第一次 +1

        // 第二次 取消
        // 是否在 set 集合？
       Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

        if (member != null && member){
            // 取消点赞
            redisTemplate.opsForSet().remove(entityLikeKey , userId);
        }else {
            // 点赞
            redisTemplate.opsForSet().add(entityLikeKey , userId);
        }


        */

