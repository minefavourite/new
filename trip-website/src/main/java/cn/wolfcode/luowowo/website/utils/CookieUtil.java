package cn.wolfcode.luowowo.website.utils;

import cn.wolfcode.luowowo.common.consts.Consts;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setPath("/");
        c.setMaxAge(Integer.valueOf((Consts.USER_INFO_TOKEN_VAI_TIME * 60) + ""));
        response.addCookie(c);
    }


    public static String getCookie(HttpServletRequest req){
        //不能设置为null
        String key="";
        Cookie[] cookies = req.getCookies();
        //要对数组进行判空操作
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if ("cookie".equals(cookie.getName())) {
                    key = cookie.getValue();
                }
            }
        }
        return key;
    }

}
