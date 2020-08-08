package pres.hjc.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.DataService;
import pres.hjc.community.tools.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/8  18:17
 * @description :
 */
@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 统计
     * 在 接入 接口之前
     * @param request request
     * @param response response
     * @param handler handler
     * @return 0 1
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // uv
        String remoteHost = request.getRemoteHost();
        dataService.recordUV(remoteHost);

        // dau
        UserPO usersPO = hostHolder.getUsersPO();
        if (usersPO != null){
            dataService.recordDAU(usersPO.getId());
        }

        return true;
    }
}
