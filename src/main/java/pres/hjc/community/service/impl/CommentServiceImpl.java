package pres.hjc.community.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import pres.hjc.community.dao.CommentMapper;
import pres.hjc.community.entity.CommentPO;
import pres.hjc.community.filter.WorldsFilter;
import pres.hjc.community.service.CommentService;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.tools.CommunityRegisterStatus;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  12:23
 * @description :
 */
@Service
@Primary
public class CommentServiceImpl implements CommentService , CommunityRegisterStatus {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private WorldsFilter worldsFilter;

    @Autowired
    private DiscussPostService discussPostService;


    @Override
    public CommentPO selectCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

    /**
     * 隔离 级别
     * 传播 机制
     * @param comment comment
     * @return rows
     */
    @Transactional(
            isolation = Isolation.READ_COMMITTED ,
            propagation = Propagation.REQUIRED ,
            rollbackFor = Exception.class)
    @Override
    public int insertComment(CommentPO comment) {

        //过滤
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 处理符号
        // 过滤词汇
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(worldsFilter.filter(comment.getContent()));

        val i = commentMapper.insertComment(comment);

        // 更新 评论 帖子
        // 数量
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            // 查询到 贴子数量
            val count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            // 更新 帖子梳数量
            discussPostService.updateCommentCount(comment.getEntityId() , count);
        }

        return i;
    }



    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    public List<CommentPO> selectCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

}
