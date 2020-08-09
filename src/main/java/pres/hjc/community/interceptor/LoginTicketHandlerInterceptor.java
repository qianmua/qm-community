package pres.hjc.community.interceptor;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CookieUtil;
import pres.hjc.community.tools.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  19:46
 * @description : 取出 凭证 缓存 用户
 */
@Component
@Slf4j
public class LoginTicketHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    /**
     * cache
     * user 渲染
     */
    @Autowired
    private HostHolder hostHolder;

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
            return true;
        }
        // object //ticketPO
        val ticket = userService.findLoginTicket(value);
        // 过期
        if (ticket == null
                || ticket.getStatus() != 0
                || ticket.getExpired().getTime() <= System.currentTimeMillis()){
            return true;
        }
        UserPO po = userService.selectById(ticket.getUserId());
//        log.info("user -> {}" , po);
        // 请求开始 便持有并  缓存
        // 持有 用户
        // cache
        hostHolder.setUsersPO(po);

        // 构建 认证 结果， 存入SecurityContext // 用域授权
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                po,
                po.getPassword(),
                userService.getAuthorities(po.getId()));
        // 存入凭证
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        return true;

    }

    /**
     * 模板渲染之前 // 取出
     * @param request request
     * @param response response
     * @param handler handler
     * @param modelAndView modelAndView
     * @throws Exception err
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // obj userPO
        val po = hostHolder.getUsersPO();
//        log.info("po -> {} " , po);

        if (null != po && modelAndView != null){
            modelAndView.addObject("loginUser" , po);
        }
    }

    /**
     * 模板渲染之后
     * 清除 user （ThreadLocal）
     * @param request request
     * @param response response
     * @param handler handler
     * @param ex ex
     * @throws Exception err
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
