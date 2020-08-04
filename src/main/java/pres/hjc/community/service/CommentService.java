package pres.hjc.community.service;

import pres.hjc.community.entity.CommentPO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  12:22
 * @description :
 */
public interface CommentService {

    CommentPO selectCommentById(int id);
    int insertComment(CommentPO comment);
    int selectCountByEntity(int entityType, int entityId);
    List<CommentPO> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);
}
