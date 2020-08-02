package pres.hjc.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    /**
     * 注册
     * @return register
     */
    @GetMapping("/register.html")
    public String getRegisterPage(){
        return "site/register";
    }
}
