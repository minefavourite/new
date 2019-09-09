package cn.wolfcode.luowowo.website;

import cn.wolfcode.luowowo.website.interceptor.ControllerInterceptor;
import cn.wolfcode.luowowo.website.utils.UserInfoArgumentResolver;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@EnableDubbo
public class WebsiteServer implements WebMvcConfigurer{

    @Bean
    public ControllerInterceptor controllerInterceptor(){
        return new ControllerInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(controllerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/lgoin.html")
                .excludePathPatterns("/userLogin")
                .excludePathPatterns("/userRegist")
                .excludePathPatterns("/regist.html")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/sendVerifyCode");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver());
    }

    @Bean
    public UserInfoArgumentResolver userInfoArgumentResolver(){
        return new UserInfoArgumentResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(WebsiteServer.class,args);
    }
}
