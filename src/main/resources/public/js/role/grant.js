$(function () {
    loadModuleInfo(); 
});


var zTreeObj;
function loadModuleInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules?roleId="+$("input[name='roleId']").val(),
        dataType:"json",
        success:function (data) {

            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    })
}


function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    // 如果checked=true，表示获取勾选的节点；如果checked=false，表示获取未勾选的节点
    var nodes= zTreeObj.getCheckedNodes(true);
    var mids="mids=";
    // 获取所有的资源的id值，mIds=1&mIds=2&mIds=3
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }

    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+$("input[name='roleId']").val(),
        dataType:"json",
        success:function (data) {
            console.log(data);
        }
    })

}