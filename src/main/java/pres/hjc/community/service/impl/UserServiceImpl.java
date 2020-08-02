package pres.hjc.community.service.impl;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pres.hjc.community.dao.UserMapper;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityUnit;
import pres.hjc.community.tools.MailClientUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:10
 * @description :
 */
@Service
@Primary
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    MailClientUtil mailClientUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    String domain;
    @Value("${server.servlet.context-path}")
    String path;

    @Override
    public UserPO selectById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 注册
     * @param userPO
     * @return
     */
    public Map<String , Object> register(UserPO userPO){
        HashMap<String, Object> map = new HashMap<>();

        if (null == userPO){
            throw new IllegalArgumentException("参数不可空");
        }
        if (StringUtils.isBlank(userPO.getPassword())) {
            map.put("null" , "空值");
        }

        // 验证 账号
        val po = userMapper.selectByName(userPO.getUsername());
        if (null != po){
            map.put("null" , "存在");
        }
        // 注册
        // 盐值
        userPO.setSalt(CommunityUnit.UUID().substring(0,5));
        userPO.setPassword(CommunityUnit.MD5(userPO.getPassword() + userPO.getSalt()));
        userPO.setType(0);
        userPO.setStatus(0);
        userPO.setActivationCode(CommunityUnit.UUID());
        userPO.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png" , new Random().nextInt(10000)));
        userPO.setCreateTime(new Date());

        userMapper.insertUser(userPO);

        // send mail
        // 激活邮件
        Context context = new Context();

        context.setVariable("email" , userPO.getEmail());
        // domain //  双端加密 // 不对称加密 + 信息摘要
        String url = domain + path + "/activation/"+ userPO.getId() + "/"+ userPO.getActivationCode();

        context.setVariable("url" , url);
        val process = templateEngine.process("/mail/activation", context);
        mailClientUtil.sendMail(userPO.getEmail(), "账号激活" , process);

        return map;
    }
}
