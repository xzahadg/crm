layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /* 表单submit的监听 */
    form.on("submit(addOrUpdateCusDevPlan)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/cus_dev_plan/add";
        if($("[name='id']").val()){
            url=ctx + "/cus_dev_plan/update";
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();

            } else {
                layer.msg(
                        res.msg, {
                            icon: 5
                        }
                    );
            }
        });
        return false;
    });
});