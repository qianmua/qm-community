package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.FollowService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/6  12:32
 * @description :
 */
//@RestController
@Controller
public class FollowController implements CommunityRegisterStatus {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @PostMapping("/follow")
//    @AuthRequired
    @ResponseBody
    public String follow(int entityType , int entityId){

        val usersPO = hostHolder.getUsersPO();

        followService.follow(usersPO.getId() , entityType , entityId);

        return CommunityUtil.getJSONString(200 , "关注啦~");
    }


    @PostMapping("/unfollow")
//    @AuthRequired
    @ResponseBody
    public String unfollow(int entityType , int entityId){
        val usersPO = hostHolder.getUsersPO();
        followService.unFollow(usersPO.getId() , entityType , entityId);

        return CommunityUtil.getJSONString(200 , "已取消");
    }


    /**
     * 关注 列表
     * @param userId userId
     * @param pageDTO pageDTO
     * @param model model
     * @return views
     */
    @GetMapping("/followees/{userId}")
    public String getFollowees(@PathVariable int userId , PageDTO pageDTO , Model model){

        val userPO = userService.selectById(userId);
        if (userPO == null){
            throw new RuntimeException("用户不存在");
        }

        model.addAttribute("user" , userPO);

        pageDTO.setPath("/followees/" + userId);
        pageDTO.setLimit(10);
        // 总页数
        pageDTO.setRows((int) followService.findFolloweeCount(userId , ENTITY_TYPE_USER));

        // 关注
        val followees = followService.findFollowees(userId, pageDTO.getOffset(), pageDTO.getLimit());
        // 查询 关注状态
        if (followees != null){
            for (Map<String, Object> v1 : followees) {
                v1.put("hasFollowed" , hasFollowed(  ((UserPO)v1.get("user")).getId() ));
            }
        }


        model.addAttribute("users" , followees);

        return "site/followee";

    }

    /**
     * 我的关注
     * @param userId userId
     * @param pageDTO pageDTO
     * @param model model
     * @return views
     */
    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable int userId , PageDTO pageDTO , Model model){

        val userPO = userService.selectById(userId);
        if (userPO == null){
            throw new RuntimeException("用户不存在");
        }

        model.addAttribute("user" , userPO);


        pageDTO.setPath("/followers/" + userId);
        pageDTO.setLimit(10);
        // 总页数
        pageDTO.setRows((int) followService.findFolloweeCount(ENTITY_TYPE_USER , userId));

        // 关注
        val followers = followService.findFollowers(userId, pageDTO.getOffset(), pageDTO.getLimit());
        // 查询 关注状态
        if (followers != null){
            for (Map<String, Object> v1 : followers) {
                v1.put("hasFollowed" , hasFollowed(  ((UserPO)v1.get("user")).getId() ));
            }
        }


        model.addAttribute("users" , followers);

        return "site/follower";

    }






    /**
     * 关注状态
     * 已关注
     * 未关注
     * @param userId
     * @return
     */
    private boolean hasFollowed(int userId){
        if (hostHolder.getUsersPO() == null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUsersPO().getId() , ENTITY_TYPE_USER , userId);
    }



}
