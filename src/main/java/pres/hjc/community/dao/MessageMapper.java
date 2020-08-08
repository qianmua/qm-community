package pres.hjc.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pres.hjc.community.entity.MessagePO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  19:23
 * @description :
 */
@Mapper
public interface MessageMapper {

    /**
     *  查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<MessagePO> selectConversations(
            @Param("userId") int userId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 查询当前用户的会话数量.
     * @param userId
     * @return
     */
    int selectConversationCount(@Param("userId") int userId);

    /**
     * 查询某个会话所包含的私信列表.
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<MessagePO> selectLetters(
            @Param("conversationId") String conversationId,
            @Param("offset") int offset,
            @Param("limit")  int limit);

    /**
     * 查询某个会话所包含的私信数量.
     * @param conversationId
     * @return
     */
    int selectLetterCount(  @Param("conversationId")String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    int selectLetterUnreadCount(  @Param("userId")int userId,  @Param("conversationId")String conversationId);

    /**
     * 新增消息
     * @param message
     * @return
     */
    int insertMessage(MessagePO message);

    /**
     * 修改消息的状态
     * @param ids
     * @param status
     * @return
     */
    int updateStatus( @Param("ids")List<Integer> ids,  @Param("status")int status);





    /*
    *
    *
    *  系统 消息 通知
    *
    *
    * */


    /**
     * 查询某个主题下最新的通知
     * @param userId
     * @param topic
     * @return
     */
    MessagePO selectLatestNotice( @Param("userId")int userId, @Param("topic") String topic);

    /**
     * 查询某个主题所包含的通知数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeCount( @Param("userId")int userId, @Param("topic")String topic);

    /**
     * 查询未读的通知的数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeUnreadCount(@Param("userId") int userId, @Param("topic") String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    List<MessagePO> selectNotices(
            @Param("userId") int userId,
            @Param("topic")  String topic,
            @Param("offset") int offset,
            @Param("limit") int limit);

}
