package pres.hjc.community.tools;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/7  17:29
 * @description :
 */
public interface KafkaCommunityConstant {



    //======================================
     //  kafka 主题
    //======================================
    /**评论*/
    String TOPIC_COMMENT = "comment";
    /**点赞*/
    String TOPIC_LIKE = "like";
    /**关注*/
    String TOPIC_FOLLOW = "follow";

    /**
     * 系统ID
     * 消息发布者
     */
    int SYSTEM_USER_ID = 1;


}
