package cn.wolfcode.luowowo.website.interceptor;

import cn.wolfcode.luowowo.common.annoations.CheckLogin;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.service.IUserService;
import cn.wolfcode.luowowo.website.utils.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //判断是否为HandlerMethod对象
       if (handler instanceof HandlerMethod){
           HandlerMethod method=(HandlerMethod) handler;
          if (method.hasMethodAnnotation(CheckLogin.class)){
              //如果有注解,判断是否登录
              String key = CookieUtil.getCookie(request);
              if (key==null){
                  response.sendRedirect("/login.html");
                  return false;
              }
          }
       }
        return true;
    }
}
