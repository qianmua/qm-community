package pres.hjc.community.interceptor;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  19:46
 * @description :
 */
@Component
@Slf4j
public class LoginTicketHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    /**
     * 验证凭证
     * @param request request
     * @param response response
     * @param handler handler
     * @return true false
     * @throws Exception err
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 取得凭证
        // String
        val value = CookieUtil.getValue(request, "ticket");
        if (StringUtils.isBlank(value)){
            return false;
        }
        // object //ticketPO
        val ticket = userService.findLoginTicket(value);
        // 过期
        if (ticket == null || ticket.getStatus() != 0 || ticket.getExpired().before(new Date())){
            return false;
        }
        UserPO po = userService.selectById(ticket.getUserId());
        // 持有 用户
        // cache


        return true;
    }
}
