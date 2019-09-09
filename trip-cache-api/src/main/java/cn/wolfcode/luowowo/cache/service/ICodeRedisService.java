package cn.wolfcode.luowowo.cache.service;

public interface ICodeRedisService {
    /**
     * 发送验证码
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 从缓存中获取验证码
     * @param phone
     * @return
     */
    String getCode(String phone);

    /**
     * 将用户信息保存到redis中
     * @param userInfo
     */
    String saveUserInfo(String userInfo);

    /**
     * 从Redis中去查询到用户的信息
     * @param key
     * @return
     */
    String getUserByCookie(String key);
}
