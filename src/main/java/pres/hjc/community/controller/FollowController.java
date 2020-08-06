package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.service.FollowService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/6  12:32
 * @description :
 */
@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @PostMapping("/follow")
//    @AuthRequired
    public String follow(int entityType , int entityId){

        val usersPO = hostHolder.getUsersPO();

        followService.follow(usersPO.getId() , entityType , entityId);

        return CommunityUtil.getJSONString(200 , "关注啦~");

    }


    @PostMapping("/unfollow")
//    @AuthRequired
    public String unfollow(int entityType , int entityId){
        val usersPO = hostHolder.getUsersPO();
        followService.unFollow(usersPO.getId() , entityType , entityId);



        return CommunityUtil.getJSONString(200 , "已取消");
    }



}
