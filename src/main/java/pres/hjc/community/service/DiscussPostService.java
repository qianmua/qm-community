package pres.hjc.community.service;

import org.apache.ibatis.annotations.Param;
import pres.hjc.community.entity.DiscussPostPO;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:05
 * @description :
 */
public interface DiscussPostService {

    List<DiscussPostPO> selectDiscussPosts(int userId, int offset, int limit);
    int selectDiscussPostRows(@Param("userId") int userId);
    int addDiscussPost(DiscussPostPO discussPostPO);
    DiscussPostPO selectDiscussPostById(@Param("id") int id);
    int updateCommentCount(int commentCount , int id);

}
