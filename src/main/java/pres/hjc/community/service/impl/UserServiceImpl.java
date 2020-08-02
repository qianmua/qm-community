package pres.hjc.community.service.impl;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pres.hjc.community.dao.LoginTicketMapper;
import pres.hjc.community.dao.UserMapper;
import pres.hjc.community.entity.LoginTicketPO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.CommunityUtil;
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
 * @description :  UserServiceImpl
 */
@Service
@Primary
public class UserServiceImpl implements UserService, CommunityRegisterStatus {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MailClientUtil mailClientUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    String domain;
    //server.servlet.context-path
    @Value("/")
    String path;

    /**
     * by id
     * @param id
     * @return
     */
    @Override
    public UserPO selectById(int id) {
        return userMapper.selectById(id);
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> insertUser(UserPO user) {
        val map = register(user);
        return map;
    }

    /**
     * 注册
     * @param userPO
     * @return
     */
    public Map<String , Object> register(UserPO userPO){
        HashMap<String, Object> map = new HashMap<>(2);

        if (null == userPO){
            throw new IllegalArgumentException("参数不可空");
        }
        if (StringUtils.isBlank(userPO.getPassword())) {
            map.put("passwordMsg" , "空值");
        }

        // 验证 账号
        val po = userMapper.selectByName(userPO.getUsername());
        if (null != po){
            map.put("usernameMsg" , "存在");
        }
        // 注册
        // 盐值
        userPO.setSalt(CommunityUtil.UUID().substring(0,5));
        userPO.setPassword(CommunityUtil.MD5(userPO.getPassword() + userPO.getSalt()));
        userPO.setType(0);
        userPO.setStatus(0);
        userPO.setActivationCode(CommunityUtil.UUID());
        userPO.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png" , new Random().nextInt(10000)));
        userPO.setCreateTime(new Date());

        userMapper.insertUser(userPO);

        // send mail
        // 激活邮件
        Context context = new Context();

        context.setVariable("email" , userPO.getEmail());
        // domain //  双端加密 // 不对称加密 + 信息摘要
        String url = domain + path + "/site/activation/"+ userPO.getId() + "/"+ userPO.getActivationCode();

        context.setVariable("url" , url);
        val process = templateEngine.process("/mail/activation", context);
        mailClientUtil.sendMail(userPO.getEmail(), "账号激活" , process);

        return map;
    }

    @Override
    public int activation(int userId , String code){
        val userPO = userMapper.selectById(userId);
        // 重复操作
        if (userPO.getStatus() == 1){
            return ACTIVACTION_ERPEAT;
        }
        // 成功
        if (userPO.getActivationCode().equals(code)){
            userMapper.updateStatus(userId , 1);
            return ACTIVACTION_SUCCESS;
        }
        // 激活失败
        return ACTIVACTION_FAILUER;
    }

    /**
     * 登录
     * @param username username
     * @param  password password
     * @param exSeconds exSeconds
     * @return Map
     */
    @Override
    public Map<String, Object> login(String username , String password , int exSeconds){

        val map = new HashMap<String, Object>(2);

        if (StringUtils.isBlank(password)){
            map.put("passwordMsg" , "null");
            return map;
        }
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg" , "null");
            return map;
        }

        // 验证账号
        val userPO = userMapper.selectByName(username);
        if (userPO == null){
            map.put("usernameMsg" , "null");
            return map;
        }
        // 注册 了 未激活
        if (userPO.getStatus() == 0){
            map.put("usernameMsg" , "null");
            return map;
        }

        // 校验密码
        password = CommunityUtil.MD5(password + userPO.getSalt());
        if (!password.equals(password)){
            map.put("usernameMsg" , "null");
            return map;
        }

        // 生成凭证
        val ticketPO = new LoginTicketPO();

        ticketPO.setUserId(userPO.getId());
        ticketPO.setTicket(CommunityUtil.UUID());
        ticketPO.setExpired(new Date((System.currentTimeMillis() + exSeconds * 1000)));
        // 状态
        ticketPO.setStatus(0);

        loginTicketMapper.insertLoginTicket(ticketPO);

        map.put("ticket" , ticketPO.getTicket());

        return map;
    }

    /**
     * 等出
     * @param ticket ticket
     */
    @Override
    public void logout(String ticket) {
        // 清除
        loginTicketMapper.updateStatus(ticket, 1);
    }

    /**
     *
     * @param ticket ticket
     * @return ob
     */
    @Override
    public LoginTicketPO findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
}
