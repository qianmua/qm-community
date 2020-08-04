package pres.hjc.community.tools;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  17:11
 * @description : 注册状态
 */
public interface CommunityRegisterStatus {

    /**
     * success
     */
    int ACTIVACTION_SUCCESS = 0;

    /**
     * repeat
     */
    int ACTIVACTION_ERPEAT = 1;

    /**
     * fail
     */
    int ACTIVACTION_FAILUER = 2;

    /**
     * 默认超时
     */
    int DEFAULT_EXPTRED = 60 * 60 * 12 ;

    /**
    * 记住我默认超时
    * 一个月
    * */
    int REMEMBER_EXPTRED = 60 * 60 * 24 * 30;

    /**
     * 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

}
