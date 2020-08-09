package pres.hjc.community.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityStatusCode;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.CookieUtil;
import pres.hjc.community.tools.GenRedisKeyUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
public class SiteController implements CommunityStatusCode {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer producerl;

    @Autowired
    private RedisTemplate redisTemplate;

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
     * //@param session
     */
    @GetMapping("/kaptcha")
    public void getkaptcha(HttpServletResponse response  /*,HttpSession session*/){
        //
        String text = producerl.createText();
        BufferedImage image = producerl.createImage(text);
        // 存放验证码
//        session.setAttribute("kaptcha" , text);

        //redis
        // 授予  实体
        String kapatchaOwner = CommunityUtil.UUID();
        Cookie cookie = new Cookie("kaptchaOwner", kapatchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);

        String redisKey = GenRedisKeyUtil.getKaptchaKey(kapatchaOwner);
        // 验证码 有效时间
        redisTemplate.opsForValue().set(redisKey ,text , 60 , TimeUnit.SECONDS);

        /// 回显验证码
        response.setContentType("image/png");
        try (ServletOutputStream stream = response.getOutputStream()){
            ImageIO.write(image , "png" , stream);
        } catch (IOException e) {
            log.error("传输失败",e.getMessage());
        }
    }

    /**
     * 登录 service
     * @param username username
     * @param password password
     * @param code code
     * @param rememberme true false
     * @param model model
     * // @param session session
     * @param kaptchaOwner cookie redis
     * @param response response
     * @return views
     */
    @PostMapping("/login")
    public String loginSystem(
            String username ,
            String password ,
            String code ,
            boolean rememberme,
            Model model ,
           /* HttpSession session ,*/
            @CookieValue("kaptchaOwner")String kaptchaOwner,
            HttpServletResponse response){

        // session
        //val kaptcha = (String)session.getAttribute("kaptcha");

        String kaptcha = null;
        // redis 获得验证码
        if (StringUtils.isNoneBlank(kaptchaOwner)){
            val kaptchaKey = GenRedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(kaptchaKey);
        }

        if (StringUtils.isBlank(kaptcha)
                || StringUtils.isBlank(code)
                || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg" , "验证码错误");
            return "site/login";
        }

        // 校验用户
        int expiredTime = rememberme ? REMEMBER_EXPTRED : DEFAULT_EXPTRED;

        //map<string , object>
        val login = userService.login(username, password, expiredTime);

        // 包含ticket
        if (login.containsKey("ticket")){
            val cookie = new Cookie("ticket", login.get("ticket").toString());
            cookie.setPath("/");
            cookie.setMaxAge(expiredTime);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            // 未登录
            model.addAttribute("usernameMsg" , login.get("usernameMsg"));
            model.addAttribute("passwordMsg" , login.get("passwordMsg"));
            return "site/login";
        }


    }


    /**
     * 登出
     * //
     * 取得 cookie 凭证
     * 清除 状态
     * //
     * @param ticket ticket
     * @return views
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request , @CookieValue("ticket")String ticket){
        // 删除 cookie
        CookieUtil.removeValue(request, ticket);
        userService.logout(ticket);
        return "redirect:/site/login";
    }


    /*@GetMapping("/profile")
    public String profileViews(){
        return "site/profile";
    }*/


}
