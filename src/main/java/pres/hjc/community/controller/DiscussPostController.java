package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.entity.DiscussPostPO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  18:22
 * @description :
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;


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
    public String getDView(@PathVariable int discussPostId , Model model){
        val postPO = discussPostService.selectDiscussPostById(discussPostId);

        model.addAttribute("post" , postPO);

        val userPO = userService.selectById(postPO.getUserId());

        model.addAttribute("user" , userPO);


        return "site/discuss-detail";

    }


}
