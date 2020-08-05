package pres.hjc.community.service;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  22:16
 * @description : 点赞
 */
public interface LikeService {
    /*点赞*/


    /**
     * 点赞
     * @param userId userId
     * @param entityType entityType
     * @param  entityId entityId
     * @param entityUserId entityUserId
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 查询某实体点赞的数量
     * @param entityType entityType
     * @param entityId entityId
     * @return num
     */
    long findEntityLikeCount(int entityType, int entityId);

    /**
     * 查询某人对某实体的点赞状态
     * @param userId userId
     * @param entityType  entityType
     * @param entityId entityId
     * @return status
     */
    int findEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * 查询某个用户获得的赞
     * @param userId userId
     * @return num
     */
    int findUserLikeCount(int userId);
}
