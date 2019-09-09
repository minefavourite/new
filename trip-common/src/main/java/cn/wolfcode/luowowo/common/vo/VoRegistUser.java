package cn.wolfcode.luowowo.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class VoRegistUser implements Serializable {
    private String phone;
    private String nickname;
    private String password;
    private String rpassword;
    private String verifyCode;
}
