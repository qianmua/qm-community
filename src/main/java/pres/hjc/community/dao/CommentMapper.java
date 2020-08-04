package pres.hjc.community.dao;

import org.apache.ibatis.annotations.Mapper;
import pres.hjc.community.entity.CommentPO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  12:19
 * @description :
 */
@Mapper
public interface CommentMapper {

    /**
     * 品论列表
     * 根据帖子类型
     * @param entityType entityType
     * @param entityId entityId
     * @param offset offset
     * @param limit limit
     * @return list
     */
    List<CommentPO> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 评论 数
     * @param entityType entityType
     * @param entityId entityId
     * @return rows
     */
    int selectCountByEntity(int entityType, int entityId);


    /**
     * 添加 评论
     * @param comment comment
     * @return rows
     */
    int insertComment(CommentPO comment);

    /**
     * 查看评论
     * @param id id
     * @return obj
     */
    CommentPO selectCommentById(int id);


}
