package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.service.LikeService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

import java.util.HashMap;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  22:30
 * @description :
 */
@Controller
//@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;



    @PostMapping("/like")
//    @AuthRequired
    public String like(int entityType , int entityId , int entityUserId , int postId){

        val usersPO = hostHolder.getUsersPO();

        likeService.like(usersPO.getId() , entityType , entityId ,0);

        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        int likeStatus = likeService.findEntityLikeStatus(usersPO.getId(), entityType, entityId);

        //vo
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("likeCount" , likeCount);
        map.put("likeStatus" , likeStatus);

        return CommunityUtil.getJSONString(200 , null , map);

    }
}
