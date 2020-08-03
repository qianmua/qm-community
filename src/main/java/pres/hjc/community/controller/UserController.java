package pres.hjc.community.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

import java.io.File;
import java.io.IOException;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  14:28
 * @description :
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

//    @Value("${server.servlet.context-path}")
    private String contextPath;





    /**
     * 账号设置
     * @return
     */
    @GetMapping("/setting")
    public String getSetting(){
        return "site/setting";
    }

    /**
     * 个人主页
     * @return
     */
    @GetMapping("/profile")
    public String profileViews(){
        return "site/profile";
    }

    /**
     * 上传头头像
     * @param headerImage
     * @param model
     * @return
     */
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage , Model model){

        if (headerImage == null){
            model.addAttribute("error" , "选择图片");
            return "site/setting";
        }

        //String fileName
        val filename = headerImage.getOriginalFilename();

        int lastIndexOf = filename.lastIndexOf(".");
        // jpg png...
        String suffer = filename.substring(lastIndexOf);

        if (StringUtils.isBlank(suffer)){
            model.addAttribute("error" , "格式不正确");
            return "site/setting";
        }
        // 文件名
        String s = CommunityUtil.UUID() + suffer;
        // upload
        File file = new File(uploadPath + "/" + filename);
        try {
            // to local
            headerImage.transferTo(file);
        } catch (IOException e) {
            log.error("upload file fail -> {}" , e.getMessage());
            throw new RuntimeException("上传文件失败" , e);
        }

        // 更新路径
        // web 访问路径
        // 虚拟路径
        UserPO po = hostHolder.getUsersPO();
        //fileName // path
        val s1 = domain + contextPath + "/user/header" + filename;
        userService.uploadHeader( po.getId(), s1);

        return "redirect:/index";
    }














}
