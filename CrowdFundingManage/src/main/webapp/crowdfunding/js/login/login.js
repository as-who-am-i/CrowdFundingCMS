function login() {
    //先获取表单的数据
    var username=$("#username").val()
    var password=$("#password").val()
    var loginMessage=$("#login-message")
    //再进行校验

    if (username.length == 0) {
        loginMessage.text("用户名不能为空")
        console.log("1111")
        return
    }
    if (password.length < 6) {
        loginMessage.text("密码位数大于6")
        return
    }
    $.ajax({
        contentType:"application/x-www-form-urlencoded;charset=UTF-8",
        url:"login/login.do",
        method:"post",
        dataType:"json",
        data:{
            username:username,
            password:md5(password)
        },
        success:function (resp) {
            if (resp.isSuccess) {
                console.log("66666")
                window.location.href="crowdfunding/user.html";
            }else {
                loginMessage.text("服务端异常，登陆失败，请重试")
            }
        },
        error:function () {
            loginMessage.text("登陆失败，请重试")
        }
    })
}

function md5(password) {
    return hex_md5(password);
}