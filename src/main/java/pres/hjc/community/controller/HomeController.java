package pres.hjc.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.DiscussPostPO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.DiscussPostService;
import pres.hjc.community.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:19
 * @description :
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    /**
     * 首页
     * @param model model
     * @return page
     */
    @GetMapping("/index")
    public String getIndexPage(Model model , PageDTO pageDTO){
        // spring将 page 注入 model // 可以直接访问
        // all rows
        pageDTO.setRows(discussPostService.selectDiscussPostRows(0));

        pageDTO.setPath("/index");

        List<DiscussPostPO> pos = discussPostService.selectDiscussPosts(0, pageDTO.getOffset(), pageDTO.getLimit());

        ArrayList<Map<String, Object>> dis = new ArrayList<>();

        // 封装
        pos.forEach( it -> {
            HashMap<String, Object> hashMap = new HashMap<>(2);
            hashMap.put("post" , it);
            UserPO po = userService.selectById(it.getUserId());
            hashMap.put("user" , po);
            dis.add(hashMap);
        });

        dis.forEach(System.out::println);

        model.addAttribute("discuss" , dis);

        return "index";
    }


}
