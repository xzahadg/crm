layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /* 关闭弹出层 */
    $("#closeBtn").click(function () {
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name);// 先得到当前iframe层的索引
        parent.layer.close(index);// 在执行关闭
    });
    /* 选择指派人的下拉框 */
    $.post(ctx+"/user/queryAllSales",function (res) {
        for(var i=0;i<res.length;i++){// 遍历集合
            if($("input[name='man']").val() == res[i].id){
                $("#assignMan").append("<option value=\""+res[i].id+"\"  selected='selected' >"+res[i].uname+"</option>");
            }else{
                $("#assignMan").append("<option value=\""+res[i].id+"\"   >"+res[i].uname+"</option>");
            }

        }
        // 重新渲染下拉框内容
        layui.form.render("select");
    });

    form.on('submit(addOrUpdateSaleChance)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{
            icon:16,// 图标
            time:false,// 不关闭
            shade:0.8// 设置遮罩的透明度
        });
        var url = ctx+"/sale_chance/add";// 添加操作
        // 通过营销机会的ID来判断当前需要执行添加操作还是修改操作
        // 如果营销机会的Id为空，则表示执行添加操作；如果Id不为空，则表示执行更新操作
        if($("input[name='id']").val()){
            // 更新操作
            url=ctx+"/sale_chance/update";
        }
        $.post(url,data.field,function (res) {
            if(res.code==200){
                top.layer.msg("操作成功");
                // 关闭加载层
                top.layer.close(index);
                // 关闭所有弹出层
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(res.msg);
            }
        });
        return false;
    });

});