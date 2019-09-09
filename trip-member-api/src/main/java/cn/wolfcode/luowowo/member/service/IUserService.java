package cn.wolfcode.luowowo.member.service;


import cn.wolfcode.luowowo.common.exception.DisableException;
import cn.wolfcode.luowowo.common.vo.VoRegistUser;
import cn.wolfcode.luowowo.member.domain.UserInfo;

import java.util.List;

public interface IUserService {
    UserInfo get(Long id);

    /**
     * 检测手机号是否存在
     *
     * @param phone
     * @return
     */
    boolean checkPhone(String phone);

    /**
     * 进行注册认证
     * @param voRegistUser
     */
    void regist(VoRegistUser voRegistUser) throws DisableException;

    /**
     * 查找是否存在用户
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password)throws DisableException;

    /**
     * 根据cookie作用域中的传递的key值去Redis中查询
     * @param key
     * @return
     */
    UserInfo getUserByCookie(String key);

    /**
     * 查询用户
     * @return
     */
    List<UserInfo> list();
}
