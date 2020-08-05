package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import pres.hjc.community.dao.MessageMapper;
import pres.hjc.community.entity.MessagePO;
import pres.hjc.community.filter.WorldsFilter;
import pres.hjc.community.service.MessageService;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  12:17
 * @description :
 */
@Service
@Primary
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    WorldsFilter worldsFilter;



    @Override
    public List<MessagePO> selectConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    @Override
    public int selectConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<MessagePO> selectLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    @Override
    public int selectLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectNoticeUnreadCount(userId, conversationId);
    }

    @Override
    public int insertMessage(MessagePO message) {
        // 过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(worldsFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int updateStatus(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    @Override
    public MessagePO selectLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    @Override
    public int selectNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    @Override
    public int selectNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    @Override
    public List<MessagePO> selectNotices(int userId, String topic, int offset, int limit) {
        return null;
    }
}
