package pres.hjc.community.service.impl;

import lombok.val;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pres.hjc.community.dao.UserMapper;
import pres.hjc.community.entity.LoginTicketPO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityRegisterStatus;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.GenRedisKeyUtil;
import pres.hjc.community.tools.MailClientUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:10
 * @description :  UserServiceImpl
 */
@Service
@Primary
public class UserServiceImpl implements UserService, CommunityRegisterStatus, UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MailClientUtil mailClientUtil;

    @Autowired
    private TemplateEngine templateEngine;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    String domain;
    //server.servlet.context-path
    @Value("/")
    String path;

    /**
     * by id
     * @param id id
     * @return obj
     */
    @Override
    public UserPO selectById(int id) {

        var cache = getCache(id);
        if (cache == null){
            cache = initCache(id);
        }
//        return userMapper.selectById(id);
        return cache;
    }


    /**
     * 注册
     * @param user user
     * @return obj
     */
    @Override
    public Map<String, Object> insertUser(UserPO user) {
        val map = register(user);
        return map;
    }

    /**
     * 注册
     * @param userPO userPO
     * @return obj
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

            // 清理原始缓存
            clearCache(userId);
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

        val map = new HashMap<String, Object>(8);

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
        ticketPO.setExpired(new Date((System.currentTimeMillis() + exSeconds )));

        System.out.println("==============");
        System.out.println(ticketPO.getExpired().getTime());


        // 状态
        ticketPO.setStatus(0);

        // 保存凭证
//        loginTicketMapper.insertLoginTicket(ticketPO);

        //redis
        String key = GenRedisKeyUtil.getTicketKey(ticketPO.getTicket());
        redisTemplate.opsForValue().set(key , ticketPO);

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
//        loginTicketMapper.updateStatus(ticket, 1);

        //redis
        String key = GenRedisKeyUtil.getTicketKey(ticket);
        LoginTicketPO ticketPO = (LoginTicketPO) redisTemplate.opsForValue().get(key);
        // 状态
        // 0 login
        // 1 loginout
        ticketPO.setStatus(1);
        redisTemplate.opsForValue().set(key , ticketPO);
    }

    /**
     *  取出凭证
     * @param ticket ticket
     * @return ob
     */
    @Override
    public LoginTicketPO findLoginTicket(String ticket) {
        String key = GenRedisKeyUtil.getTicketKey(ticket);

//        return loginTicketMapper.selectByTicket(ticket);
        return (LoginTicketPO) redisTemplate.opsForValue().get(key);
    }


    /**
     * 更新 头像
     * @param userId
     * @param headerUrl
     * @return
     */
    @Override
    public int uploadHeader(int userId , String headerUrl){
//        int i = userMapper.updateHeader(userId, headerUrl);
//         清理原始缓存
//        clearCache(userId);
        return userMapper.updateHeader(userId, headerUrl);
//        return i;
    }

    @Override
    public UserPO selectByName(String username) {
        return userMapper.selectByName(username);
    }

    @Override
    public UserPO selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public int updateHeader(int id, String headerUrl) {
        int i = userMapper.updateHeader(id, headerUrl);
        //         清理原始缓存
        clearCache(id);
        return i;
    }


    /**
     * 从redis 中 取得 缓存 的 user
     * @param userId userId
     * @return user
     */
    private UserPO getCache(int userId){
        String  key = GenRedisKeyUtil.getUserKey(userId);
        return (UserPO) redisTemplate.opsForValue().get(key);
    }

    /**
     * 给redis 缓存 user
     * @param userId userId
     * @return user
     */
    private UserPO initCache(int userId){
        val userPO = userMapper.selectById(userId);
        String  key = GenRedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(key , userPO ,3600 * 3 , TimeUnit.SECONDS);
        return userPO;
    }

    /**
     * 清除 缓存用户信息
     * @param userId userId
     */
    private void clearCache(int userId){
        String  key = GenRedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(key);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
