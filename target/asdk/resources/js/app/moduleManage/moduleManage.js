var id = 0;
var node;
$(document).ready(function () {
    var basePath = $("base").attr("href");
    //显示tree
    getTree(basePath);
    //新增node
    $("#add").click(function () {
        $('#editWindow').modal({backdrop: 'static', keyboard: false});
        $("#editTitle").html('添加模块节点');
        $("#id").val(0);
        $("#editForm")[0].reset();
        if(node&&node.nodes){
            $("[name=pid]").append(new Option( node.text,id, true)).trigger("change");
        }else{
            $("[name=pid]").val("").trigger("change");
        }
    });
    //修改node
    $("#edit").click(function () {
        if(!node){
            bootbox.alert("错误! 请选择要修改的模块");
            return;
        }
        //首先获取一个
        $.ajax({
            url:basePath + '/admin/module/getModuleByID',
            data:{
                currModuleID: id
            },
            dataType:'json',
            success:function (data) {
                $("[name=name]").val(data.name);
                $("[name=id]").val(data.id);
                $("[name=url]").val(data.url);
                $("[name=iconCls]").val(data.iconCls);
                $("[name=pid]").append(new Option( data.pName,data.pid , true)).trigger("change");
                $("[name=leaf]").val(data.leaf+"").trigger("change");
                $("#url").attr("disabled",!data.leaf);
                $("[name=permission]").val(data.permission).trigger("change");
                $('#editWindow').modal({backdrop: 'static', keyboard: false});
                $("#editTitle").html('修改模块');
            }
        });
    });
    //删除node
    $("#remove").click(function () {
        if(!node){
            bootbox.alert("错误! 请选择要删除的模块");
            return;
        }
        bootbox.confirm('你确定要删除 '+node.text+' 吗?',function (value) {
            console.debug(node);
            if (value){
                $.ajax({
                    url:basePath+"/admin/module/removeModule",
                    data:{
                        id:id
                    },
                    dataType:'json',
                    success:function (res) {
                        if(res.state == 1){
                            getTree(basePath);
                        }else {
                            bootbox.alert(res.msg);
                        }
                    }
                });
            }
        })
    });
    //表单提交
    $("#editForm").bootstrapValidator({
        submitHandler: function(validator, form) {
            $.post(form.attr('action'), form.serialize(), function(result) {
                bootbox.alert(result.msg);
                $("[type=submit]").attr('disabled',false);
                if(result.state == 1){
                    $('#editWindow').modal("hide");
                    getTree(basePath);
                }
            }, 'json');
        }
    });
});

function getTree(basePath) {
    $('#tree').treeview({
        data: getData(basePath),
        onNodeSelected:function (event , data) {
            id = data.href.substring(1);
            node = data;
        }
    });
}

//获取树形Data
function getData(basePath) {
    var tree;
    $.ajax({
        url: basePath + '/admin/module/getModulesTreeType',
        success: function (data) {
            tree = data;
        },
        async: false
    });
    return tree;
}