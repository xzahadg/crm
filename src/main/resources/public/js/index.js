layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    // 进行登录操作
    /**
     * 表单submit提交
     *  form.om('submit(按钮的lay-filter属性值)'，function(data){
     *      console.log(data.elem)// 被执行事件的元素DOM对象，一般为button对象
     *      console.log(data.form)// 被执行提交的form对象，一般存在form标签时才会返回
     *      console.log(data.field)// 当前容器的全部表单字段，名 值对形式（name:value）
     *  })
     */
    form.on('submit(login)', function (data) {
        data = data.field;
        if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
            layer.msg('用户名不能为空');
            return false;
        }
        if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
            layer.msg('密码不能为空');
            return false;
        }
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:data.username,
                userPwd:data.password
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    layer.msg('登录成功', function () {
                        var result =data.result;
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("trueName",result.trueName);
                        // 如果点击记住我 设置cookie 过期时间7天
                        if($("input[type='checkbox']").is(':checked')){
                            // 写入cookie 7天
                            $.cookie("userIdStr",result.userIdStr, { expires: 7 });
                            $.cookie("userName",result.userName, { expires: 7 });
                            $.cookie("trueName",result.trueName, { expires: 7 });
                        }
                        window.location.href=ctx+"/main";
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
});