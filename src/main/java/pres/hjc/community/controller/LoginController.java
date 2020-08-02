package pres.hjc.community.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  15:15
 * @description :
 */
@Controller
@RequestMapping("/site")
@Slf4j
public class LoginController implements CommunityRegisterStatus {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer producerl;

    /**
     * 注册
     * @return register
     */
    @GetMapping("/register.html")
    public String getRegisterPage(){
        return "site/register";
    }

    /**
     * login
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "site/login";
    }


    /**
     * 注册 server
     * @param model
     * @param userPO
     * @return
     */
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

    /**
     * 注册验证 邮箱
     * @param model model
     * @param userId userId
     * @param code code
     * @return views
     */
    @GetMapping("/activation/{userId}/{code}")
    public String activation(
            Model model,
            @PathVariable("userId")Integer userId ,
            @PathVariable("code")String code){
        val i = userService.activation(userId, code);

        if (i == ACTIVACTION_SUCCESS){
            model.addAttribute("msg" , "激活成功");
            model.addAttribute("target" , "/login");
        }
        if (i == ACTIVACTION_ERPEAT){
            model.addAttribute("msg" , "无效操作");
            model.addAttribute("target" , "/index");
        }
        if (i == ACTIVACTION_FAILUER){
            model.addAttribute("msg" , "激活失败");
            model.addAttribute("target" , "/index");
        }

        return "site/operate-result";
    }


    /**
     * 生成验证码
     * @param response
     * @param session
     */
    @GetMapping("/kaptcha")
    public void getkaptcha(HttpServletResponse response , HttpSession session){
        //
        String text = producerl.createText();
        BufferedImage image = producerl.createImage(text);
        // 存放验证码
        session.setAttribute("kaptcha" , text);

        /// 回显验证码
        response.setContentType("image/png");
        try (ServletOutputStream stream = response.getOutputStream()){
            ImageIO.write(image , "png" , stream);
        } catch (IOException e) {
            log.error("传输失败",e.getMessage());
        }
    }
}
