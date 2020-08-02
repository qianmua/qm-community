package pres.hjc.community.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  19:40
 * @description :
 */
@Component
@Slf4j
public class BaseHandlerInterceptor implements HandlerInterceptor {

    /**
     * before
     * @param request request
     * @param response response
     * @param handler handler
     * @return suf
     * @throws Exception err
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle" + handler.toString());
        return true;
    }

    /**
     * 渲染之后
     * @param request request
     * @param response response
     * @param handler handler
     * @param ex err
     * @throws Exception err
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * after
     * @param request request
     * @param response response
     * @param handler handler
     * @param modelAndView modelAndView
     * @throws Exception err
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
