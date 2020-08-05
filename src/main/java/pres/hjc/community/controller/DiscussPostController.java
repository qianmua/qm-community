package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.CommentPO;
import pres.hjc.community.entity.DiscussPostPO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.CommentService;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.service.LikeService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

import java.util.*;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  18:22
 * @description :
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityRegisterStatus {

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    /**
     * 发帖
     * @param title title
     * @param content content
     * @return views
     */
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title , String content){
        UserPO po = hostHolder.getUsersPO();
        if (null == po){
            return CommunityUtil.getJSONString(403 , "未登录");
        }

        DiscussPostPO po1 = new DiscussPostPO();
        po1.setId(po1.getId());
        po1.setTitle(title);
        po1.setContent(content);
        po1.setCreateTime(new Date());

        discussPostService.addDiscussPost(po1);

        return CommunityUtil.getJSONString(200 , "success");
    }


    /**
     * 阅读
     * redis 缓存 提高效率
     * @param discussPostId
     * @param model
     * @return
     */
    @GetMapping("/detail/{discussPostId}")
    public String getDView(@PathVariable int discussPostId , Model model , PageDTO pageDTO){
        val postPO = discussPostService.selectDiscussPostById(discussPostId);

        // 帖子
        model.addAttribute("post" , postPO);

        // 作者
        val userPO = userService.selectById(postPO.getUserId());

        // 点赞
        // type
        // id (帖子Id)
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount" , likeCount);
        // 状态
        // 当前用户 的 带你赞状态
        // 注意不是 帖子作者

        int likeStatus = hostHolder.getUsersPO() == null ? 0 :
                likeService.findEntityLikeStatus(
                hostHolder.getUsersPO().getId(),ENTITY_TYPE_POST , discussPostId);
        model.addAttribute("likeStatus" , likeStatus);


        model.addAttribute("user" , userPO);

        //评论
        pageDTO.setLimit(10);
        pageDTO.setPath("/discuss/detail/" + discussPostId);
        pageDTO.setRows(postPO.getCommentCount());

        // 所有 评论
        List<CommentPO> commentPOS = commentService.selectCommentsByEntity(
                ENTITY_TYPE_POST, postPO.getId(), pageDTO.getOffset(), pageDTO.getLimit());
        List<Map<String , Object>> commentVOs = new ArrayList<>();

        commentPOS.forEach(v1 -> {
            HashMap<String, Object> vo = new HashMap<>(2);
            vo.put("comment" , v1);
            // 评论作者
            vo.put("user" , userService.selectById(v1.getUserId()));

            // 点赞
            vo.put("likeCount" , likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, v1.getId()));
            //状态
            vo.put("likeStatus" , hostHolder.getUsersPO() == null ? 0 :
                    likeService.findEntityLikeStatus(
                            hostHolder.getUsersPO().getId(),ENTITY_TYPE_COMMENT, v1.getId()));

            // 回复列表
            List<CommentPO> pos = commentService.selectCommentsByEntity(
                    ENTITY_TYPE_COMMENT, v1.getId(), 0, Integer.MAX_VALUE);
            // VO
           List<Map<String , Object>> reVO = new ArrayList<>();
            pos.forEach(v2 -> {
                // 回复
                val map = new HashMap<String, Object>(2);
                map.put("reply" , v2);
                map.put("user" , userService.selectById(v2.getUserId()));

                // 点赞
                map.put("likeCount" , likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, v2.getId()));
                //状态
                map.put("likeStatus" , hostHolder.getUsersPO() == null ? 0 :
                        likeService.findEntityLikeStatus(
                                hostHolder.getUsersPO().getId(),ENTITY_TYPE_COMMENT, v2.getId()));


                // 回复目标
                UserPO po = v2.getTargetId() == 0 ? null : userService.selectById(v2.getTargetId());
                map.put("target" , po);
                reVO.add(map);
            });
            // 回复
            vo.put("replys" , reVO);
            //回复数量
            val count = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, v1.getId());
            vo.put("replyCount" , count);
            // =========================
            commentVOs.add(vo);
        });

        model.addAttribute("comments" , commentVOs);

        return "site/discuss-detail";

    }


}
