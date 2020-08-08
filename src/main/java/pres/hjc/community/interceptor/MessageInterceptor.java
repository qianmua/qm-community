package pres.hjc.community.interceptor;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pres.hjc.community.service.MessageService;
import pres.hjc.community.tools.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/8  15:44
 * @description :
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    /**
     * 消息 未读 提前消费
     * 触发 事件
     * 渲染时
     * @param request request
     * @param response response
     * @param handler handler
     * @param modelAndView modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        val usersPO = hostHolder.getUsersPO();

        if (usersPO != null && modelAndView != null){
            val i = messageService.selectLetterUnreadCount(usersPO.getId(), null);
            val i1 = messageService.selectNoticeUnreadCount(usersPO.getId(), null);
            modelAndView.addObject("allUnreadCount" , (i1 + i) );
        }
    }
}
