package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  15:15
 * @description :
 */
@Controller
@RequestMapping("/site")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     * @return register
     */
    @GetMapping("/register.html")
    public String getRegisterPage(){
        return "site/register";
    }

    @PostMapping("/register")
    public String register(Model model , UserPO userPO){
        val map = userService.insertUser(userPO);
        if (map != null && !map.isEmpty()){
            model.addAttribute("usernameMsg" , map.get("usernameMsg"));
            model.addAttribute("passwordMsg" , map.get("usernameMsg"));
            model.addAttribute("emailMsg" , map.get("usernameMsg"));
            return "site/register";
        }
        model.addAttribute("msg" , "注册成功，请查看您的邮箱，尽快激活");
        model.addAttribute("target" , "/index");
        return "site/operate-result";

    }
}
