package cn.wolfcode.luowowo.common.utils;

import cn.wolfcode.luowowo.common.exception.DisableException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 断言工具类, 一些判断操作
 */
public class AssertUtil {


    /**
     * 判断传入value值是否有值
     * @param value
     * @param msg
     * @return
     */
    public static void hasLength(String value, String msg) {
        if(value== null || "".equals(value.trim())){
            throw new DisableException(msg);
        }
    }

    /**
     * 判断v1 跟 v2 是否一致
     * @param valueOne
     * @param valueTwo
     * @param msg
     */
    public static void isEquals(String valueOne, String valueTwo, String msg) {

        if(valueOne == null || valueTwo == null){
            throw new DisableException("传入判断参数不能为空");
        }

        if(!valueOne.equals(valueTwo)){
            throw new DisableException(msg);
        }

    }

    public static String jiami(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
