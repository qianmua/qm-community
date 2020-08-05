package pres.hjc.community.exception;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pres.hjc.community.tools.CommunityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  19:50
 * @description :
 */
@ControllerAdvice( annotations = {Controller.class})
@Slf4j
public class ExceptionController {

    /**
     * 全部异常处理
     * @param e e
     */
    @ExceptionHandler({Exception.class})
    public void handlerException(Exception e , HttpServletRequest request , HttpServletResponse response) throws IOException {

        log.error("server error -> {} " , e.getMessage());

        //异常信息
        // element // 每行
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }


        // 异步请求 分别处理
        val header = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(header)){
            response.setContentType("application/plain;charset=utf-8");
            response.getWriter().write(CommunityUtil.getJSONString(500 , "服务出错"));
        }else {
            // 错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }


    }
}
