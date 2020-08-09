package pres.hjc.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pres.hjc.community.interceptor.*;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  19:35
 * @description : app config
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BaseHandlerInterceptor baseHandlerInterceptor;

//    @Autowired
//    private LoginTicketHandlerInterceptor loginTicketHandlerInterceptor;

    @Autowired
    private AuthRequiredHandlerInterceptor authRequiredHandlerInterceptor;
    @Autowired
    private MessageInterceptor messageInterceptor;
    @Autowired
    private DataInterceptor dataInterceptor;

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseHandlerInterceptor)
                .excludePathPatterns("/**/*.css" , "/**/*.js" , "/img/**")
                .addPathPatterns("register" , "/login");

        //security
        //registry.addInterceptor(loginTicketHandlerInterceptor)
        //        .excludePathPatterns("/**/*.css" , "/**/*.js" , "/img/**");


        registry.addInterceptor(authRequiredHandlerInterceptor)
                .excludePathPatterns("/**/*.css" , "/**/*.js" , "/img/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css" , "/**/*.js" , "/img/**");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css" , "/**/*.js" , "/img/**");
    }
}
