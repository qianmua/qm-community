package pres.hjc.community.dao;

import org.apache.ibatis.annotations.*;
import pres.hjc.community.entity.LoginTicketPO;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  18:14
 * @description : 登录 凭证 // 类似 jwt
 */
@Mapper
public interface LoginTicketMapper {

    /**
     * 登录状态 新增
     * @param loginTicket loginTicket
     * @return rows
     */
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicketPO loginTicket);


    /**
     * 校验状态
     * @param ticket ticket
     * @return  LoginTicketPO
     */
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicketPO selectByTicket( @Param("ticket")String ticket);

    /**
     * 更新 ， 生命周期0.0
     * 好好的 学什么java
     * // 动态sql
     * // 有个 kotlin 版 的 可以 参考参考
     * @param ticket ticket
     * @param status status
     * @return rows
     */
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(@Param("ticket") String ticket, @Param("status")int status);
}
