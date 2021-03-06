package pres.hjc.community.service;

import org.springframework.security.core.GrantedAuthority;
import pres.hjc.community.entity.LoginTicketPO;
import pres.hjc.community.entity.UserPO;

import java.util.Collection;
import java.util.Map;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:07
 * @description :
 */
public interface UserService{

    default UserPO selectById(int id){
        return null;
    }
    default UserPO selectByName(String username){
        return null;
    }
    default UserPO selectByEmail(String email){
        return null;
    }

    default Map<String, Object> insertUser(UserPO user){
        return null;
    }

    default int updateStatus(int id, int status){
        return -1;
    }
    default int updateHeader(int id, String headerUrl){
        return -1;
    }
    default int updatePassword(int id, String password){
        return -1;
    }

    int activation(int userId , String code);


    /**
     * 登录
     * @param username
     * @param password
     * @param exSeconds
     * @return
     */
    Map<String, Object> login(String username , String password , int exSeconds);

    /**
     * 登出
     * @param ticket ticket
     */
    void logout(String ticket);

    /**
     * 查找凭证
     * @param ticket
     * @return
     */
    LoginTicketPO findLoginTicket(String ticket);


    /**
     * 上传头像
     * @param userId
     * @param headerUrl
     * @return
     */
    int uploadHeader(int userId , String headerUrl);

    /**
     * 权限集
     * @param userId userId
     * @return auth
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId);




}
