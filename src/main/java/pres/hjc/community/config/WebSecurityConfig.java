package pres.hjc.community.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import pres.hjc.community.service.impl.UserServiceImpl;
import pres.hjc.community.tools.CommunityStatusCode;
import pres.hjc.community.tools.CommunityUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/9  13:39
 * @description :
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements CommunityStatusCode {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源 过滤
        web.ignoring().antMatchers("/resources/**");
    }

    /**
     * 认证
     * 配置规则
     *  AuthenticationManager
     *  默认规则 providerManager (负责认证规则发布 / 委托模式)
     * @param auth 认证接口，用与构建Manage工具
     * @throws Exception err
     */
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 内置
        auth.userDetailsService(userService).passwordEncoder(new Pbkdf2PasswordEncoder("123456"));

        // 自定义规则
        // AuthenticationProvider
        // providerManager 持有一组 AuthenticationProvider
        // 每个AuthenticationProvider 负责 一种认证

        auth.authenticationProvider(new AuthenticationProvider() {
            *//**
             * 账号密码认证
             * 认证逻辑
             * @param authentication 认证信息 接口
             * @return info
             * @throws AuthenticationException e
             *//*
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                // 认证逻辑
                // 封装了 账号密码

                val name = authentication.getName();
                val credentials = (String) authentication.getCredentials();
                // load by sqlDB
                val userDetails = userService.loadUserByUsername(credentials);

                if (userDetails == null){
                    throw new UsernameNotFoundException("账号密码 不存在。");
                }

                val md5Password = CommunityUtil.MD5(credentials);
                if (!userDetails.getPassword().equals(md5Password)){
                    throw new BadCredentialsException("密码不正确");
                }
                // 认证结果
                // p1 : 主要信息 ；
                // p2 : 证书
                // p3 : 权限
                return new UsernamePasswordAuthenticationToken(
                        userDetails ,
                        userDetails.getPassword() ,
                        userDetails.getAuthorities());
            }

            *//**
             * 支持 的认证结果类型
             *
             * param authentication authentication
             * @return  1 0
             *//*
            @Override
            public boolean supports(Class<?> authentication) {
                // UsernamePasswordAuthenticationToken
                // authentication 的 常用实现类
                return UsernamePasswordAuthenticationToken.class.equals(authentication);
            }
        });
    }*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权逻辑

        /*http.formLogin()
                // get
                .loginPage("/site/login")
                // post
                .loginProcessingUrl("/site/login")
                // authenticationSuccessHandler
                .successHandler(null)
                // authenticationSuccessHandler
                .failureHandler(null)
                ;*/


        // 默认 拦截 logout url
        // 覆盖默认逻辑
        // 定制
        http.logout()
                .logoutUrl("/test/site/logout")
                // logoutSuccessHandler
//                .logoutSuccessHandler(null)
                ;


        http.authorizeRequests()
                .antMatchers(
                        "user/setting" , "/user/upload" ,
                        "/discuss/add" , "/comment/add/**" ,
                        "letter/**", "/notice/**","/like",
                        "/follow" , "/unfollow"
                ).hasAnyAuthority(
                        AUTHORITY_USER ,
                AUTHORITY_ADMIN ,
                AUTHORITY_MODERATOR,
                AUTHORITY_ROOT)
                .antMatchers(
                        "/discuss/top",
                "/discuss/wonderful").hasAnyAuthority(AUTHORITY_MODERATOR)
                .antMatchers("/discuss/delete").hasAnyAuthority(AUTHORITY_ADMIN)

                .anyRequest().permitAll()
        .and()

        .csrf().disable()
        ;

                /*.antMatchers("/letter").hasAnyAuthority("ROOT" , "ADMIN")
                .antMatchers("/admin").hasAnyAuthority("ROOT" , "ADMIN")
                */
                // 无权限？
        http.exceptionHandling()
                // 未登录
                .authenticationEntryPoint((request, response, authException) -> {
                    // ajax or http?
                    String header = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(header)){
                        response.setContentType("application/plain;charset=utf-8");
                        response.getWriter().write(CommunityUtil.getJSONString(403 , "未登录"));
                    }else {
                        response.sendRedirect(request.getContextPath() + "/site/login");
                    }

                })
                // 权限不足
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String header = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(header)){
                        response.setContentType("application/plain;charset=utf-8");
                        response.getWriter().write(CommunityUtil.getJSONString(403 , "权限不足"));
                    }else {
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                })
                // 403
                /*.accessDeniedPage("/denied")*/;

        // 验证码处理
        // filter
        //request -> ServletRequest
        // 是 httpServletRequest父接口
        http.addFilterBefore((request, response, chain) -> {
            // 从请求中去到验证码
            // 然后再去 验证

            // 继续执行
            // 向下
            chain.doFilter(request , response);

        }, UsernamePasswordAuthenticationFilter.class);


        /*http.rememberMe()
                // token 存到内存中
                // 自定义实现 TokenRepository
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds(3600 * 24)
                // 记住我实现
                // 自动认证
                .userDetailsService(userService)
                // 安全 cookie // https
                .useSecureCookie(false);*/


    }
}
