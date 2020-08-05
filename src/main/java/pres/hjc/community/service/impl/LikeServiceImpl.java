package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
     * @param userId userId
     * @param entityType entityType
     * @param  entityId entityId
     * @param entityUserId entityUserId
     */
    @Override
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);

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
//        String entityLikeKey = GenRedisKeyUtil.getEntityLikeKey(entityType , entityId);

        return 0;
    }
}
