package cn.wolfcode.luowowo.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装json对象
 */
@Setter
@Getter
@AllArgsConstructor
public class JsonResult {
    public static final int CODE_TYPE_UNLOGIN = 102;  //没有登录
    private boolean success=true;
    private String msg;
    private Object object;
    private Object data;
    private Integer code;

    public JsonResult(){}

    public JsonResult(boolean success,String msg){
        this.success=success;
        this.msg=msg;
    }
    public JsonResult addData(Object data){
        this.data = data;
        return this;
    }

    public JsonResult(String msg, int code){
        this.success = false;
        this.msg = msg;
        this.code = code;
    }
}
