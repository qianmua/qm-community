package pres.hjc.community.tools;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  22:11
 * @description :
 */
public class GenRedisKeyUtil {

    /**
     * 分隔符号
     */
    public static final String SPLIT = ":";

    /**
     * 点赞 喜欢
     */
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
    /**
     * 用户 点赞
     */
    public static final String PREFIX_USER_LIKE = "like:user";

    /**
     *
     * 我关注的
     */
    public static final String PREFIX_FOLLOWEE = "followee";
    /**
     * 关注我的
     */
    public static final String PREFIX_FOLLOWER = "follower";


    /**
     * 我关注的
     * followee:uid:entityType
     * zset (sort)
     *
     * @param entityType entityId
     * @return key
     */
    public static String getFolloweeKey(int userId , int entityType){
        return PREFIX_FOLLOWEE +SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 关注我的
     * follower:entityType:entityId
     * zset
     * type：谁拥有的 粉丝
     * id：
     *
     * value （userId , nowTime）
     * @param entityType entityType
     * @param entityId entityId
     * @return key
     */
    public static String getFollowerKey(int entityType , int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    /**
     * 用户的点赞
     * like:user:id -> int
     * string
     * @param userId userId
     * @return key
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 有关 谁 的 赞
     * //
     * 某个 实体的赞
     *
     * entityType:
     * 回复 2
     * 帖子 1
     * ..
     *
     *
     * like:entity:entityType:id
     * set方式
     * @param entityType entityType
     * @param entityId entityId
     * @return key
     */
    public static String getEntityLikeKey(int entityType , int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }


}
