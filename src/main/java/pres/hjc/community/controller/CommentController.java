package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.entity.CommentPO;
import pres.hjc.community.event.EventProducer;
import pres.hjc.community.service.CommentService;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.HostHolder;
import pres.hjc.community.tools.ObjectCommunityConstant;
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
public class CommentController implements ObjectCommunityConstant , CommunityRegisterStatus {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;


    @Autowired
    private DiscussPostService discussPostService;


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

        if (commentPO.getEntityType() == ENTITY_TYPE_POST){
            val discussPostPO = discussPostService.selectDiscussPostById(commentPO.getEntityId());
            // 通知给 -》
            eventVO.setEntityUserId(discussPostPO.getUserId());
        }else if (commentPO.getEntityType() == ENTITY_TYPE_USER){

        }

        return "redirect:/discuss/detail/" + disId ;
    }
}
