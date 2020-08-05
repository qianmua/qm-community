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
