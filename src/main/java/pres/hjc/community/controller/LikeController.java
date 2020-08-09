package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pres.hjc.community.event.EventProducer;
import pres.hjc.community.service.LikeService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;
import pres.hjc.community.tools.KafkaCommunityConstant;
import pres.hjc.community.vo.EventVO;

import java.util.HashMap;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  22:30
 * @description :
 */
@RestController
//@Controller
//@RequestMapping("/like")

public class LikeController implements KafkaCommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;


    /**
     * 点赞 处理
     * @param entityType entityType
     * @param entityId entityId
     * @param entityUserId entityUserId
     * @param postId postId
     * @return json (status)
     */
    @PostMapping("/like")
//    @AuthRequired
    public String like(int entityType , int entityId , int entityUserId , int postId){

        val usersPO = hostHolder.getUsersPO();

        likeService.like(usersPO.getId() , entityType , entityId ,entityUserId);

        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        int likeStatus = likeService.findEntityLikeStatus(usersPO.getId(), entityType, entityId);

        //vo
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("likeCount" , likeCount);
        map.put("likeStatus" , likeStatus);

        // 系统事件 消费
        if (likeStatus == 1){
            val eventVO = new EventVO()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUsersPO().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId" , postId);

            eventProducer.fireEvent(eventVO);
        }

        return CommunityUtil.getJSONString(200 , null , map);
    }
}
