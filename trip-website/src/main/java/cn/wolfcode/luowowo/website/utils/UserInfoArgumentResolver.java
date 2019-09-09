package cn.wolfcode.luowowo.website.utils;

import cn.wolfcode.luowowo.common.annoations.UserParam;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.service.IUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Reference
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserParam.class) &&methodParameter.getParameterType() == UserInfo.class;
    }
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String key = CookieUtil.getCookie(request);
        if(key != null){
            return userService.getUserByCookie(key);
        }
        return null;
    }
}