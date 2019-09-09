package cn.wolfcode.luowowo.member.service.impl;

import cn.wolfcode.luowowo.cache.service.ICodeRedisService;
import cn.wolfcode.luowowo.common.exception.DisableException;
import cn.wolfcode.luowowo.common.utils.AssertUtil;
import cn.wolfcode.luowowo.common.vo.VoRegistUser;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.mapper.UserInfoMapper;
import cn.wolfcode.luowowo.member.service.IUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Reference
    private ICodeRedisService codeRedisService;

    @Override
    public UserInfo get(Long id) {
        return userInfoMapper.selectByPrimaryKey(1L);
    }

    @Override
    public boolean checkPhone(String phone) {

        int i = userInfoMapper.checkPhone(phone);
        return i>0;
    }

    @Override
    public void regist(VoRegistUser voRegistUser)  {
        //进行判空操作
        AssertUtil.hasLength(voRegistUser.getPhone(),"手机号不能为空");
        AssertUtil.hasLength(voRegistUser.getNickname(),"昵称不能为空");
        AssertUtil.hasLength(voRegistUser.getPassword(),"密码不能为空");
        AssertUtil.hasLength(voRegistUser.getRpassword(),"请输入第二次密码");
        AssertUtil.hasLength(voRegistUser.getVerifyCode(),"验证码不能为空");

        //检测手机号是否存在
        if (userInfoMapper.checkPhone(voRegistUser.getPhone())>0){
            throw new DisableException("手机号已存在");
        }
        //判断验证码是否一致
       String phone=voRegistUser.getPhone();
        String code = codeRedisService.getCode(phone);
        AssertUtil.isEquals(voRegistUser.getVerifyCode(),code,"验证码错误");
        //进行注册
        UserInfo user=new UserInfo();
        user.setHeadImgUrl("/images/default.jpg");
        user.setLevel(1);
        user.setState(UserInfo.STATE_NORMAL);
        user.setNickname(voRegistUser.getNickname());
       String password= voRegistUser.getPassword();
        String jiami = AssertUtil.jiami(password);
        user.setPassword(jiami);
        user.setPhone(voRegistUser.getPhone());
        userInfoMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        //检测是否存在用户
        UserInfo userInfo = userInfoMapper.loginByUsernamePassword(username, password);
        if (userInfo==null){
            throw new DisableException("账号或密码错误");
        }
        String userInfoRedis = JSON.toJSONString(userInfo);
        //将用户信息存到redis中
        String token = codeRedisService.saveUserInfo(userInfoRedis);
        //将令牌存到cookie中
        return token;
    }

    @Override
    public UserInfo getUserByCookie(String key) {

        String userByCookie = codeRedisService.getUserByCookie(key);
        UserInfo userInfo = JSON.parseObject(userByCookie, UserInfo.class);
        return userInfo;
    }

    @Override
    public List<UserInfo> list() {
        List<UserInfo> userInfos = userInfoMapper.selectAll();
        return userInfos;
    }

}
