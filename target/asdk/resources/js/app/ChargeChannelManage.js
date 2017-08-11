//全局Ajax事件
var basePath = $("base").attr("href");
$(document).ajaxComplete(function(event , xhr){
    var text = xhr.responseText;
    if(text.indexOf("<!DOCTYPE html>")>-1){
        window.location=basePath+"/admin/login";
    }
    if(text.indexOf("操作失败")>-1){
        window.location=basePath+"/admin/login";
    }
    if(xhr.status!=200){
        bootbox.alert('通信错误!<br>代码:'+xhr.status);
    }
});

$(document).ready(function () {
    $('#addBtn').click(function(){
        $('#editWindow').modal({backdrop: 'static', keyboard: false});
        $("#editTitle").html('添加充值渠道商');
        $("#id").val(0);
        $("#editForm")[0].reset();
    });

    $("#editForm").bootstrapValidator({
        submitHandler: function(validator, form) {
            $.post(form.attr('action'), form.serialize(), function(result) {
                bootbox.alert(result.msg);
                $("[type=submit]").attr('disabled',false);
                if(result.state == 1){
                    $('#editWindow').modal("hide");
                    $("#tables").bootstrapTable('refresh');
                }
            }, 'json');
        }
    });
});
function actionFormatter(value, row, index) {
    return [
        '<a class="edit ml10" href="javascript:void(0)" title="编辑">',
        '<i class="fa fa-pencil fa-fw"></i>',
        '</a>',
        '<a class="remove ml10" href="javascript:void(0)" title="删除">',
        '<i class="fa fa-trash-o fa-fw"></i>',
        '</a>'
    ].join('');
}
function responseHandler(res){
    return res;
}
window.actionEvents = {
    'click .edit': function (e, value, row) {
        $('#editWindow').modal({backdrop: 'static', keyboard: false});
        $("#editTitle").html('修改游戏');
        $("#editForm")[0].reset();
        var inputs = $("input");
        for(var i in inputs){
            var value = row[inputs[i].name];
            if(value!=undefined){
                $(inputs[i]).val(value);
            }
        }
        $('#adminID').append(new Option(row.adminName , row.adminID, true)).trigger('change');
    },
    'click .remove': function (e, value, row) {
        var id = row.id;
        bootbox.confirm("你确定要删除[充值渠道:"+row.chargeChannelName+"]吗 , 此操作不可恢复", function(result){
            if(result==true){
                $.ajax({
                    url:basePath+'/admin/chargeChannel/removeChannel',
                    data:{
                        currChargeChannelID:id
                    },
                    type:"POST",
                    dataType:'json',
                    success:function(data){
                        bootbox.alert(data.msg);
                        if(data.state==1){
                            $("#tables").bootstrapTable('remove',{field:'id' , values:[id]});
                        }
                    }
                });
            }
        });
    }
};