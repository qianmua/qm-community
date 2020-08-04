package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pres.hjc.community.dao.CommentMapper;
import pres.hjc.community.entity.CommentPO;
import pres.hjc.community.service.CommentService;

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
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public CommentPO selectCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

    @Override
    public int insertComment(CommentPO comment) {
        return commentMapper.insertComment(comment);
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
