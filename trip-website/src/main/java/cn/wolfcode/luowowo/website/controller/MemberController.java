package cn.wolfcode.luowowo.website.controller;

import cn.wolfcode.luowowo.cache.service.ICodeRedisService;
import cn.wolfcode.luowowo.common.annoations.CheckLogin;
import cn.wolfcode.luowowo.common.consts.Consts;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import cn.wolfcode.luowowo.common.vo.VoRegistUser;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.service.IUserService;
import cn.wolfcode.luowowo.website.utils.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MemberController {

    @Reference
    private IUserService userService;

    @Reference
    private ICodeRedisService codeRedisService;


    //进行手机号判断
    @RequestMapping("/checkPhone")
    @ResponseBody
    public Object check(String phone){
        boolean bool = userService.checkPhone(phone);
        return bool;
    }

    //生成验证码
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public JsonResult sendVerifyCode(String phone){
        JsonResult result=new JsonResult();
        codeRedisService.sendVerifyCode(phone);
        return result;
    }

    //进行注册
    @RequestMapping("/userRegist")
    @ResponseBody
    public JsonResult regist(VoRegistUser voRegistUser){
        JsonResult result=new JsonResult();
        userService.regist(voRegistUser);
        return result;
    }

    //用户登录
    @RequestMapping("/userLogin")
    @ResponseBody
    public JsonResult login(String username,String password,HttpServletResponse response){
        JsonResult result=new JsonResult();
        String token = userService.login(username, password);
        CookieUtil.addCookie(response,"cookie",token);
        return result;
    }


}
