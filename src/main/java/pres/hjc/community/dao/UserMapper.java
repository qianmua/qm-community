package pres.hjc.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pres.hjc.community.entity.UserPO;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:02
 * @description :
 */
@Mapper
public interface UserMapper {

    /**
     * user by id
     * @param id id
     * @return user
     */
    UserPO selectById(int id);

    /**
     * by name
     * @param username username
     * @return user
     */
    UserPO selectByName(String username);

    /**
     * by email
     * @param email email
     * @return user
     */
    UserPO selectByEmail(String email);

    /**
     * add user
     * @param user user
     * @return rows
     */
    int insertUser(UserPO user);

    /**
     * update status
     * @param id id
     * @param status status
     * @return rows
     */
    int updateStatus(int id, int status);

    /**
     * update info
     * @param id id
     * @param headerUrl headerUrl
     * @return rows
     */
    int updateHeader(@Param("id") int id, @Param("headerUrl") String headerUrl);

    /**
     * change password
     * @param id id
     * @param password password
     * @return rows
     */
    int updatePassword(@Param("id")int id, @Param("password")String password);
}
