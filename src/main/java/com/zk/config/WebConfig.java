package com.zk.config;

import com.zk.interceptor.Interceptor_;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*解读：解决跨域*/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("x-requested-with", "content-type")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor_())
                .addPathPatterns("/")//拦截所有请求
                .excludePathPatterns("/", "/login",
                        "/admin/login", "/admin/register",
                        "/css/**", "/js/**", "/img/**", "/index.html",
                        "/saveO", "/notify_url.php", "/return_url.php",
                        "/listPdctU", "/listByCidPdct", "/admin/listCtg", "/admin/getAncmt"); //指定要放行的请求，后面可以根据业务进行调整
    }
}

