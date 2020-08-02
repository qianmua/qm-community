package pres.hjc.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pres.hjc.community.entity.DiscussPostPO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:00
 * @description :
 */
@Mapper
public interface DiscussPostMapper {

    /**
     * 查询 列表
     * @param userId  userId
     * @param offset offset
     * @param limit limit
     * @return list<列表>
     */
    List<DiscussPostPO> selectDiscussPosts(
            @Param("userId") int userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * 查询 条数
     * @param userId userId
     * @return rows
     */
    int selectDiscussPostRows(@Param("userId") int userId);
}
