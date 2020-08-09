package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.entity.CommentPO;
import pres.hjc.community.event.EventProducer;
import pres.hjc.community.service.CommentService;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.tools.CommunityStatusCode;
import pres.hjc.community.tools.GenRedisKeyUtil;
import pres.hjc.community.tools.HostHolder;
import pres.hjc.community.tools.KafkaCommunityConstant;
import pres.hjc.community.vo.EventVO;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/4  18:13
 * @description :
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements KafkaCommunityConstant, CommunityStatusCode {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 添加 评论
     * //
     * 传入 添加的 当前 编号
     * 希望 后面 直接定位到 页面取
     * @param disId
     * @param commentPO
     * @return
     */
    @PostMapping("/add/{disId}")
    @AuthRequired
    public String saveComment(@PathVariable int disId , CommentPO commentPO){

        commentPO.setUserId(hostHolder.getUsersPO().getId());
        commentPO.setStatus(0);
        commentPO.setCreateTime(new Date());
        commentService.insertComment(commentPO);

        // 评论事件
        // 通知消费
        val eventVO = new EventVO()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUsersPO().getId())
                .setEntityId(commentPO.getEntityId())
                .setEntityType(commentPO.getEntityType())
                .setData("postId" , disId);

        // 通知类型
        if (commentPO.getEntityType() == ENTITY_TYPE_POST){
            val discussPostPO = discussPostService.selectDiscussPostById(commentPO.getEntityId());
            // 通知给 -》用户
            eventVO.setEntityUserId(discussPostPO.getUserId());
            // 排行分
            redisTemplate.opsForSet().add(GenRedisKeyUtil.getPostScore() , disId);

        }else if (commentPO.getEntityType() == ENTITY_TYPE_COMMENT){
            // 评论
            val commentPO1 = commentService.selectCommentById(commentPO.getEntityId());
            eventVO.setEntityUserId(commentPO1.getUserId());
        }

        // 消息 发布
        eventProducer.fireEvent(eventVO);

        return "redirect:/discuss/detail/" + disId ;
    }
}
