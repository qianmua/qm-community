package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import pres.hjc.community.dao.DiscussPostMapper;
import pres.hjc.community.entity.DiscussPostPO;
import pres.hjc.community.filter.WorldsFilter;
import pres.hjc.community.service.DiscussPostService;

import java.util.List;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:05
 * @description :
 */
@Service
@Primary
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    WorldsFilter worldsFilter;

    @Override
    public List<DiscussPostPO> selectDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public int addDiscussPost(DiscussPostPO discussPostPO) {
        if (discussPostPO == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        //转义 html标记
        // --
        discussPostPO.setTitle(HtmlUtils.htmlEscape(discussPostPO.getTitle()));
        discussPostPO.setContent(HtmlUtils.htmlEscape(discussPostPO.getContent()));

        // 过滤敏感词
        discussPostPO.setTitle(worldsFilter.filter(discussPostPO.getTitle()));
        discussPostPO.setContent(worldsFilter.filter(discussPostPO.getContent()));

        // 添加数据
        return discussPostMapper.insertDiscussPost(discussPostPO);
    }

    @Override
    public DiscussPostPO selectDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    @Override
    public int updateCommentCount(int commentCount, int id) {
        return 0;
    }
}
