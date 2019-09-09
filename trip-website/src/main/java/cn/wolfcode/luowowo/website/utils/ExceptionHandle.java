package cn.wolfcode.luowowo.website.utils;

import cn.wolfcode.luowowo.common.exception.DisableException;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 进行统一的异常处理
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(DisableException.class)
    public JsonResult handleDisEx( HttpServletResponse response,DisableException dx)throws Exception {
        String message = dx.getMessage();
        /*response.setContentType("text/json;character=utf-8");
        response.getWriter().write(JSON.toJSONString(new JsonResult(false,message)));*/
        return new JsonResult(false, message);
    }

    @ExceptionHandler(Exception.class)
    public void handleEx(Exception e ,HttpServletResponse response) throws Exception {
        e.printStackTrace();
        String message = e.getMessage();
        response.setContentType("text/json;character=utf-8");
        response.getWriter().write(JSON.toJSONString(new JsonResult(false,message)));

    }
}
