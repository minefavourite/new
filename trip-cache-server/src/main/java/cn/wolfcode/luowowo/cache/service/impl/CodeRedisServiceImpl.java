package cn.wolfcode.luowowo.cache.service.impl;

import cn.wolfcode.luowowo.cache.service.ICodeRedisService;
import cn.wolfcode.luowowo.common.consts.Consts;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import cn.wolfcode.luowowo.common.utils.AssertUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CodeRedisServiceImpl implements ICodeRedisService {

    @Autowired
    private StringRedisTemplate template;

    @Override
    public void sendVerifyCode(String phone) {
        //判断手机号是否为空
         AssertUtil.hasLength(phone,"手机号不能为空");
        //设置验证码
            String code = UUID.randomUUID().toString().substring(0, 5);
            System.out.println(code);
            //将验证码存到redis中
        String key = ConstantEnum.append(ConstantEnum.VERIFY_CODE.getName(), phone);
        template.opsForValue().set(key,code,Consts.VERIFY_CODE_VAI_TIME*60, TimeUnit.SECONDS);
            //拼接验证码发送短信
        StringBuilder builder=new StringBuilder();
            builder.append("尊敬的用户你的验证码为:").append(builder).append(",验证码有效时间为")
                    .append(Consts.VERIFY_CODE_VAI_TIME);

    }

    @Override
    public String getCode(String phone) {
        String key= ConstantEnum.append(ConstantEnum.VERIFY_CODE.getName(), phone);
        String code = template.opsForValue().get(key);
        return code;
    }

    @Override
    public String saveUserInfo( String userInfo) {
        //将随机数作为key
        String string = UUID.randomUUID().toString().replace("-","");
        String key = ConstantEnum.append(ConstantEnum.USER_INFO_REDIS.getName(), string);
        template.opsForValue().set(key,userInfo,ConstantEnum.USER_INFO_REDIS.getTime(),TimeUnit.SECONDS);
        return key;
    }

    @Override
    public String getUserByCookie(String key) {
        //根据传递进来的value查询值
        String userInfo = template.opsForValue().get(key);
        System.out.println(key);
        template.expire(key,30*60,TimeUnit.SECONDS);
        return userInfo;
    }
}
