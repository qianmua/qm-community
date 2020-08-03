package pres.hjc.community.interceptor;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pres.hjc.community.annotation.AuthRequired;
import pres.hjc.community.tools.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  16:13
 * @description :
 */
@Component
public class AuthRequiredHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    /**
     * 权限
     * @param request request
     * @param response response
     * @param handler handler
     * @return false
     * @throws Exception err
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 是一个方法？
        if (! (handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        val method = handlerMethod.getMethod();
        val annotation = method.getAnnotation(AuthRequired.class);
        if ( null == annotation){
            return true;
        }
        if ( hostHolder.getUsersPO() != null){
            return true;
        }

        // 无权限
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
