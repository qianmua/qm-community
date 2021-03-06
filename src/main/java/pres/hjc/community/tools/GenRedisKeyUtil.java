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
    private static final String SPLIT = ":";

    /**
     * 点赞 喜欢
     */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    /**
     * 用户 点赞
     */
    private static final String PREFIX_USER_LIKE = "like:user";

    /**
     *
     * 我关注的
     */
    private static final String PREFIX_FOLLOWEE = "followee";
    /**
     * 关注我的
     */
    private static final String PREFIX_FOLLOWER = "follower";

    /**
     * 验证码
     */
    private static final String PREFIX_KAPTCHA = "kaptcha";

    /**
     * 凭证
     */
    private static final String PREFIX_TICKET = "ticket";
    /**
     * 用户信息
     */
    private static final String PREFIX_USER = "user";

    /**
     * 独立访客
     * ip
     * hyperLogLog
     */
    private static final String PREFIX_UV = "uv";

    /**
     * 日活跃用户
     * userId
     * bitMap
     */
    private static final String PREFIX_DAU = "dau";

    /**
     * 帖子排行
     */
    private static final String PREFIX_POST_SCORE = "post";


    /*
    ***************************************
    ***************************************
    *               key
    ***************************************
    ***************************************
    * */

    /**
     * 热帖 key
     * @return key
     */
    public static String getPostScore(){
        return PREFIX_POST_SCORE + SPLIT + "score";
    }

    /**
     * 单日 UV
     * @param date
     * @return
     */
    public static String getUVKey(String date){
        return PREFIX_UV + SPLIT + date;
    }

    /**
     * 日期区间：
     * @param sDate
     * @param lDate
     * @return
     */
    public static String getUVKey(String sDate , String lDate){
        return PREFIX_UV +SPLIT + sDate + SPLIT + lDate;
    }

    /**
     * 单日 日活跃
     * @param date
     * @return
     */
    public static String getDAUKey(String date){
        return PREFIX_DAU + SPLIT + date;
    }

    /**
     * 指定日期 活跃
     * @param sDate
     * @param lDate
     * @return
     */
    public static String getDAUKey(String sDate , String lDate){
        return PREFIX_DAU + SPLIT + sDate + SPLIT + lDate;
    }



    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

    /**
     * 登录凭证
     * @param ticket ticket
     * @return key
     */
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET +SPLIT + ticket;
    }

    /**
     * 登录 验证码
     * @param owner 用户标识
     * @return key
     */
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }




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
