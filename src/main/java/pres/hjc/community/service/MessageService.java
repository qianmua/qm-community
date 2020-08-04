package pres.hjc.community.service;

import pres.hjc.community.entity.MessagePO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  19:25
 * @description :
 */
public interface MessageService {

    List<MessagePO> selectConversations(int userId, int offset, int limit);

    int selectConversationCount(int userId);

    List<MessagePO> selectLetters(String conversationId, int offset, int limit);

    int selectLetterCount(String conversationId);

    int selectLetterUnreadCount(int userId, String conversationId);

    int insertMessage(MessagePO message);

    int updateStatus(List<Integer> ids, int status);

    MessagePO selectLatestNotice(int userId, String topic);

    int selectNoticeCount(int userId, String topic);

    int selectNoticeUnreadCount(int userId, String topic);

    List<MessagePO> selectNotices(int userId, String topic, int offset, int limit);
}
