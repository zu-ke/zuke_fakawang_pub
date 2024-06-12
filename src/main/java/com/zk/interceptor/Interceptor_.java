package com.zk.interceptor;

import com.zk.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class Interceptor_ implements HandlerInterceptor {

    /*解读：目标方法执行前别执行*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //看一下访问的url
        String requestURI = request.getRequestURI();
        log.info("preHandle拦截到的请求的URI: " + requestURI);

        //进行登录验证
        Object admin = request.getSession().getAttribute("username");
        if (admin == null) {
            // 使用HttpServletResponse发送重定向
            response.sendRedirect("/");
            return false; // 返回false，拦截请求
        }
        return true; // 已登录，请求继续
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行了");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion执行了");
    }
}
