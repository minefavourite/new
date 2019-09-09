
$(function () {
    $('#_js_loginBtn').click(function () {
        var val = $('#inputPassword').val();

        //js 正则表达语法:   /^1   $/


        if (/^1\d{10}$/g.test(val)) {

            $.get("/checkPhone", {phone:val}, function (data) {
                if(!data){
                    $('#inputPassword').next().text('').hide()
                    $('.login-box').hide()
                    $('.signup-box').show()
                    $("#phone").val(val);
                }else{
                    $('#inputPassword').next().text('手机号码已注册.').show()
                }
            })
        } else {
            $('#inputPassword').next().text('手机号码格式不正确').show()
        }
    });

    //短信发送
    $('.vcode-send').click(function () {
        if ($(this).hasClass('disabled')) {
        } else {
            var self = $(this);
            var count = 10;
            self.addClass('disabled')
            self.text(count + '秒后重新获取')
            var timer = setInterval(function () {
                count--;
                if (count > 0) {
                    self.text(count + '秒后重新获取');
                } else {
                    clearInterval(timer)
                    self.text('重新获取验证码')
                    self.removeClass("disabled");
                }

            }, 1000);

            var phone = $("#phone").val();
            $.get("/sendVerifyCode", {phone:phone}, function (data) {

                console.log(data);
                if(data.success){
                    popup("发送成功")
                }else{
                    popup(data.msg);
                }
            })
        }
    });

    //注册
    $("#editForm").ajaxForm(function (data) {
        if(data.success){
            location.href = "/login.html";
        }else{
            popup(data.msg);
        }
    })
    $("#editForm").validate({
        rules:{
            nickname: {
                required: true,
                minlength: 2
            },
            password: {
                required: true,
                rangelength: [1, 10]
            },
            repassword: {
                required: true,
                equalTo: "#password"
            },
            verifyCode: {
                required: true,
                rangelength: [4,4]
            }

        },
        messages:{
            nickname: {
                required: "昵称必须要填写",
                minlength: "长度最小为1"
            },
            password: {
                required: "密码必须填写",
                rangelength:"长度在1和10之间"
            },
            repassword: {
                required: "必须验证密码",
                equalTo: "密码必须和上面相同"
            },
            verifyCode: {
                required: "验证码必须要填",
                rangelength: "长度必须为4位"
            }
        }
    });

});