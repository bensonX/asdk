<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

%>
<base href="<%=basePath%>">
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>ASDK - 渠道管理</title>
    <!-- Bootstrap Core CSS -->
    <link href="<%=basePath%>/resources/css/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="<%=basePath%>/resources/css/metisMenu.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="<%=basePath%>/resources/css/sb-admin-2.css" rel="stylesheet">
    <!-- Morris Charts CSS -->
    <link href="<%=basePath%>/resources/css/morris.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="<%=basePath%>/resources/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <!-- bootstrap-tables CSS -->
    <link href="<%=basePath%>/resources/css/bootstrap-table.css" rel="stylesheet" type="text/css">
    <!-- Select2 CSS -->
    <link href="<%=basePath%>/resources/css/select2.min.css" rel="stylesheet" />
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link href="<%=basePath%>/resources/css/tsdk.style.css" rel="stylesheet" />

    <!-- jQuery -->
    <script src="<%=basePath%>/resources/js/jquery.min.js" type="text/javascript" ></script>
    <script src="<%=basePath%>/resources/js/jquery.form.js" type="text/javascript" ></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<%=basePath%>/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- Select2 JS-->
    <script src="<%=basePath%>/resources/js/select2.full.min.js" type="text/javascript" ></script>
    <!--bootbox JS-->
    <script src="<%=basePath%>/resources/js/bootbox.js" type="text/javascript" ></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="<%=basePath%>/resources/js/metisMenu.min.js" type="text/javascript"></script>
    <!-- bootstrap-table -->
    <script src="<%=basePath%>/resources/js/bootstrap-table.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/bootstrap-table-zh-CN.js" type="text/javascript"></script>
    <!-- Custom Theme JavaScript -->
    <script src="<%=basePath%>/resources/js/sb-admin-2.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/jquery.md5.js"  type="text/javascript"></script>
</head>

<body>
    <!-- wrapper -->
    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<%=basePath%>/admin/index">ASDK</a>
            </div>
            <!-- /.navbar-header -->

            <!-- /.navbar-top-links -->
            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        ${modules}
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    你好 :
                    <c:if test="${admin==null}">
                        请重新登录
                    </c:if>
                    <c:if test="${admin!=null}">
                        ${admin.fullName}
                    </c:if>
                </li>
                <li class="dropdown">
                    <a href="<%=basePath%>/admin/logout"><i class="fa fa-sign-out fa-fw"></i>登出</a>
                </li>
            </ul>
        </nav>
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">渠道管理</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div id="toolbar" class="btn-group">
                        <button type="button" class="btn btn-default" id="addBtn">
                            <i class="fa fa-plus fa-fw"></i>
                        </button>
                    </div>
                    <table id="tables" data-toggle="table"
                           data-url="<%=basePath%>/admin/channels/getAllChannels"
                           data-query-params="queryParams"
                           data-page-size="100"
                           data-pagination="true"
                           data-side-pagination="com.u8.server"
                           data-content-type="application/x-www-form-urlencoded"
                           data-method="post"
                           data-response-handler="responseHandler"
                           data-show-refresh="true"
                           data-show-toggle="true"
                           data-show-columns="true"
                           data-toolbar="#toolbar"
                           data-height="600">
                        <thead>
                        <tr>
                            <th data-field="action" data-formatter="actionFormatter" data-events="actionEvents">操作</th>
                            <th data-field="channelID">channelID</th>
                            <th data-field="channelName">渠道名称</th>
                            <th data-field="masterName">渠道商名称</th>
                            <th data-field="appName">游戏名称</th>
                            <th data-field="cpID" data-visible="false">CPID</th>
                            <th data-field="cpAppID" data-visible="false">CP App ID</th>
                            <th data-field="cpAppKey" data-visible="false">CP App Key</th>
                            <th data-field="cpAppSecret" data-visible="false">CP App Secret</th>
                            <th data-field="cpPayKey" data-visible="false">CP Pay Key</th>
                            <th data-field="goodsConfigID" data-visible="false">商品配置ID</th>
                            <th data-visible="false" data-field="createAdminID">create Admin ID</th>
                            <th data-field="createAdminName">所有者</th>
                            <th data-visible="false">特殊配置</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
    <!-- 增加渠道面板 -->
    <div class="modal fade" id="editWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">&times;
                    </button>
                    <h4 class="modal-title" id="editTitle">
                        添加渠道
                    </h4>
                </div>
                <div class="modal-body">
                    <form id="editForm" method="post" action="<%=basePath%>/admin/channels/saveChannel">
                        <input class="form-control" name="channelID" type="hidden" id="id">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="form-group">
                                    <label>所属游戏 :</label>
                                    <select class="js-example-basic-hide-search" id="games" style="width: 100%" name="appID"></select>
                                    <p class="help-block">所属游戏 , 若该列表中没有将要选择的游戏,请在 [游戏管理] 中添加</p>
                                </div>
                            </div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>渠道商 :</label>
                                <select class="js-example-basic-hide-search" id="masters" style="width: 100%" name="masterID"></select>
                                <p class="help-block">该游戏所属渠道商 ,通俗点说就是将要接的API类型 , 若该列表中没有将要选择的渠道商,请在 [渠道商管理] 中添加</p>
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>商品配置 :</label>
                                <select class="js-example-basic-hide-search" id="goodsConfig" style="width: 100%" name="goodsConfigID"></select>
                                <p class="help-block">商品配置 , 若没有将要选择的商品配置 , 请在 [商品配置] 中添加</p>
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>渠道名称</label>
                                <input class="form-control" name="channelName">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CPID</label>
                                <input class="form-control" name="cpID">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP App ID</label>
                                <input class="form-control" name="cpAppID">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP App Key</label>
                                <input class="form-control" name="cpAppKey">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP App Sercet</label>
                                <input class="form-control" name="cpAppSecret">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP Pay ID</label>
                                <input class="form-control" name="cpPayID">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP Pay Key</label>
                                <textarea id="ppk" class="form-control" rows="3" name="cpPayKey"></textarea>
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>CP PayPriKey</label>
                                <input class="form-control" name="cpPayPriKey">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>特殊配置</label>
                                <input class="form-control" name="cpConfig">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>所有者 :</label>
                                <select class="js-example-basic-hide-search" id="createAdminID" style="width: 100%" name="createAdminID"></select>
                                <p class="help-block">完全公开的代表所有用户可见 , 分配给谁代表该用户及其子级用户可见</p>
                            </div></div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="subBtn" type="button" class="btn btn-primary"><i class="glyphicon glyphicon-floppy-disk"></i></button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <!-- 增加END -->
    <script>
        //全局Ajax事件
        $(document).ajaxComplete(function(event , xhr){
            var text = xhr.responseText;
            if(text.indexOf("<!DOCTYPE html>")>-1){
                window.location="<%=basePath%>/admin/login";
            }
            if(xhr.status!=200){
                bootbox.alert('通信错误!<br>代码:'+xhr.status);
            }
        });
        $(document).ready(function () {
            $("#editForm").ajaxForm();
            $('#games').select2({
                minimumResultsForSearch: Infinity,
                ajax:{
                    url:'<%=basePath%>/admin/games/getAllGamesSimple',
                    dataType:'json',
                    delay: 250,
                    processResults: function (data, params) {
                        var results = [];
                        for(var i in data){
                            results.push({
                                id:data[i].appID,
                                text:data[i].name
                            });
                        }
                        return {
                            results:results
                        };
                    },
                    cache: true
                }
            }).on('select2:select', function (evt) {
                var value = evt.params.data.text;
                var masterName = $("#masters").select2("data")[0];
                var field = $("[name=channelName]");
                if(masterName&&!field.val()){
                    field.val(value + " - "+masterName.text);
                }
            });
            $('#masters').select2({
                minimumResultsForSearch: Infinity,
                ajax:{
                    url:'<%=basePath%>/admin/channelMaster/getAllMastersSimple',
                    dataType:'json',
                    delay: 250,
                    processResults: function (data, params) {
                        var results = [];
                        for(var i in data){
                            results.push({
                                id:data[i].masterID,
                                text:data[i].masterName
                            });
                        }
                        return {
                            results:results
                        };
                    },
                    cache: true
                }
            }).on('select2:select', function (evt) {
                var value = evt.params.data.text;
                var gameName = $("#games").select2("data")[0];
                var field = $("[name=channelName]");
                if(gameName&&!field.val()){
                    field.val(gameName.text + " - "+value);
                }
            });
            $('#goodsConfig').select2({
                minimumResultsForSearch: Infinity,
                ajax:{
                    url:'<%=basePath%>/admin/goodsConfig/getAllConfigSimple',
                    dataType:'json',
                    delay: 250,
                    processResults: function (data) {
                        var results = [];
                        for(var i in data){
                            results.push({
                                id:data[i].goodsConfigID,
                                text:data[i].goodsConfigName
                            });
                        }
                        return {
                            results:results
                        };
                    },
                    cache: true
                }
            });
            $('#createAdminID').select2({
                minimumResultsForSearch: Infinity,
                ajax:{
                    url:'<%=basePath%>/admin/manage/getAdmins',
                    dataType:'json',
                    delay: 250,
                    processResults: function (data) {
                        var results = [];
                        results.push({
                            id:-1,
                            text:'完全公开'
                        });
                        results.push({
                            id:${admin.id},
                            text:'${admin.fullName}'
                        });
                        for(var i in data){
                            results.push({
                                id:data[i].id,
                                text:data[i].fullName
                            });
                        }
                        return {
                            results:results
                        };
                    },
                    cache: true
                }
            });
            $('#masters').change(function () {
                var value = $("#masters").val();
                if(!value){
                    return;
                }
                changeField(value);
            });
            $('#addBtn').click(function(){
                $('#editWindow').modal({backdrop: 'static', keyboard: false});
                $("#editTitle").html('添加渠道');
                $("#id").val(null);
                $("#editForm")[0].reset();
                hideAllField();
            });
            $('#subBtn').click(function(){
                $('#editForm').ajaxSubmit({
                    beforeSubmit:function(){
                        //获取所有表单中的item
                        var inputs = $("#editForm").find("input");
                        for(var i = 0 ; i < inputs.length ; i++){
                            var input = $(inputs[i]);
                            if(input.closest(".col-lg-12").css("display")!="none"&&input.attr("name")!="channelID"&&(input.val()==null||input.val()=="")){
                                bootbox.alert("请完整填写表单!("+input.attr("name")+"为空)");
                                return false;
                            }
                        }
                        inputs = $("#editForm").find("select");
                        for(var i = 0 ; i < inputs.length ; i++){
                            var input = $(inputs[i]);
                            if(input.val()==null||input.val()==""){
                                bootbox.alert("请完整填写表单!");
                                return false;
                            }
                        }
                        return true;
                    },
                    data:{
                        allgames:$("[name=appID]").val(),
                        allmasters:$('[name=masterID]').val()
                    },
                    dataType:'json',
                    success: function (data) {
                        if(data.state==1){
                            $('#editWindow').modal("hide");
                        }
                        bootbox.alert(data.msg);
                        $("#tables").bootstrapTable('refresh');
                    }
                });
            });

        });

        function changeField(masterID){
            $.ajax({
                url:'<%=basePath%>/admin/channels/getMasterValidField',
                data:{
                    currChannelID:masterID
                },
                type:"POST",
                success:function(data){
                    hideAllField();
                    if(data){
                        var fields = data.split(",");
                        //隐藏所有
                        //显示要填的字段
                        for(var i in  fields){
                            var field = fields[i].trim();
                            if(field == "cpPayKey"){
                                $("#ppk").closest(".col-lg-12").show();
                                continue;
                            }
                            $("[name="+field+"]").closest(".col-lg-12").show();
                        }
                    }
                }
            });
        }
        function showAllField(){
            var inputs = $("#editForm").find("input");
            $("#ppk").closest(".col-lg-12").show();
            for(var i=0 ; i< inputs.length ; i++){
                $($(inputs[i]).closest(".col-lg-12")).show();
            }
        }
        function hideAllField(){
            var inputs = $("#editForm").find("input");
            $("#ppk").closest(".col-lg-12").hide();
            for(var i=0 ; i< inputs.length ; i++){
                if(inputs[i].name!="appID"&&inputs[i].name!="masterID"&&inputs[i].name!="goodsConfigID"&&inputs[i].name!="channelName"){
                    $($(inputs[i]).closest(".col-lg-12")).hide();
                }
            }
        }
        function queryParams(params) {
            var page = params.offset;
            page = (page)/params.limit+1;
            return {
                rows: params.limit,
                page: page
            };
        }

        function queryGoodsParams(params){
            var page = params.offset;
            page = (page)/params.limit+1;
            return {
                rows: params.limit,
                page: page,
                channelID:Number($("#goodsChannelID").val()),
                acc:Number($("#goodsChannelID").val())
            };
        }

        function responseHandler(res){
            return res;
        }

        function actionFormatter() {
            return [
                '<a class="edit ml10" href="javascript:void(0)" title="编辑">',
                '<i class="fa fa-pencil fa-fw"></i>',
                '</a>',
                '<a class="remove ml10" href="javascript:void(0)" title="删除">',
                '<i class="fa fa-trash-o fa-fw"></i>',
                '</a>'
            ].join('');
        }

        window.actionEvents = {
            'click .edit': function (e, values, row) {
                $('#editWindow').modal({backdrop: 'static', keyboard: false});
                $("#editTitle").html('修改渠道');
                $("#editForm")[0].reset();
                var inputs = $("input");
                for(var i in inputs){
                    var value = row[inputs[i].name];
                    if(value!=undefined){
                        $(inputs[i]).val(value);
                    }
                }
                $("#ppk").val(row.cpPayKey);
                $('#games').append(new Option(row.appName,row.appID, true));
                $('#games').trigger('change');
                console.debug(row);
                $('#masters').append(new Option(row.masterName , row.masterID, true));
                $('#masters').trigger('change');
                $('#goodsConfig').append(new Option(row.goodsConfigName , row.goodsConfigID, true));
                $('#goodsConfig').trigger('change');
                $('#createAdminID').append(new Option(row.createAdminName , row.createAdminID, true));
                $('#createAdminID').trigger('change');
            },
            'click .remove': function (e, value, row) {
                var id = row.channelID;
                bootbox.confirm("你确定要删除[渠道:"+row.masterName+" , 游戏: "+row.appName+"]吗 , 此操作不可恢复", function(result){
                    if(result==true){
                        $.ajax({
                            url:'<%=basePath%>/admin/channels/removeChannel',
                            data:{
                                currChannelID:id
                            },
                            type:"POST",
                            dataType:'json',
                            success:function(data){
                                bootbox.alert(data.msg);
                                if(data.state==1){
                                    $("#tables").bootstrapTable('remove',{field:'channelID' , values:[id]});
                                }
                            }
                        });
                    }
                });
            }
        };
    </script>
</body>
</html>
