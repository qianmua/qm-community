package pres.hjc.community.service;

import java.util.List;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/6  12:17
 * @description :
 */
public interface FollowService {

    /**
     * 关注
     * @param userId userId
     * @param entityType entityType
     * @param entityId entityId
     */
    void follow(int userId, int entityType, int entityId);

    /**
     * 取消关注
     * @param userId userId
     * @param entityType entityType
     * @param entityId entityId
     */
    void unFollow(int userId, int entityType, int entityId);

    /**
     * 关注的实体的数量
     * @param userId userId
     * @param entityType entityType
     * @return size
     */
    long findFolloweeCount(int userId, int entityType);

    /**
     * 实体的粉丝的数量
     * @param entityType entityType
     * @param entityId entityId
     * @return size
     */
    long findFollowerCount(int entityType, int entityId);

    /**
     * 当前用户是否已关注该实体
     * @param userId userId
     * @param entityType entityType
     * @param entityId entityId
     * @return true || false
     */
    boolean hasFollowed(int userId, int entityType, int entityId);

    /**
     * 某用户关注的人
     * @param userId userId
     * @param offset offset
     * @param limit limit
     * @return obj
     */
    List<Map<String, Object>> findFollowees(int userId, int offset, int limit);


    /**
     * 某用户的粉丝
     * @param userId userId
     * @param offset offset
     * @param limit limit
     * @return obj
     */
    List<Map<String, Object>> findFollowers(int userId, int offset, int limit);


}
