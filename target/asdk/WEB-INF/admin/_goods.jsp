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

    <title>ASDK - 商品管理</title>
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
    <link href="<%=basePath%>/resources/css/select2.min.css" rel="stylesheet"/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link href="<%=basePath%>/resources/css/tsdk.style.css" rel="stylesheet"/>
    <!-- jQuery -->
    <script src="<%=basePath%>/resources/js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/jquery.form.js" type="text/javascript"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<%=basePath%>/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- Select2 JS-->
    <script src="<%=basePath%>/resources/js/select2.full.min.js" type="text/javascript"></script>
    <!--bootbox JS-->
    <script src="<%=basePath%>/resources/js/bootbox.js" type="text/javascript"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="<%=basePath%>/resources/js/metisMenu.min.js" type="text/javascript"></script>
    <!-- bootstrap-table -->
    <script src="<%=basePath%>/resources/js/bootstrap-table.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/bootstrap-table-zh-CN.js" type="text/javascript"></script>
    <!-- Custom Theme JavaScript -->
    <script src="<%=basePath%>/resources/js/sb-admin-2.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/jquery.md5.js" type="text/javascript"></script>
</head>

<body>

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
                    <h1 class="page-header">商品配置</h1>
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
                           data-url="<%=basePath%>/admin/goodsConfig/getAllConfig"
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
                            <th data-field="id">ID</th>
                            <th data-field="name">配置名称</th>
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

    <!-- 增加商品配置面板 -->
    <div class="modal fade" id="editWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">&times;
                    </button>
                    <h4 class="modal-title" id="editTitle">
                        添加商品配置
                    </h4>
                </div>
                <div class="modal-body">
                    <form id="editForm">
                        <input class="form-control" name="id" type="hidden" id="id">
                        <div class="row">
                            <div class="col-lg-12"><div class="form-group">
                                <label>配置名称</label>
                                <input class="form-control" name="name">
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
    <!-- 增加商品配置面板结束 -->

    <!--增加商品面板-->
    <div class="modal fade" id="addGoodsWin" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="goodsAddTitle">
                        添加商品
                    </h4>
                </div>
                <div class="modal-body">
                    <form id="goodsForm">
                        <div class="form-group">
                            <label>商品名称</label>
                            <input class="form-control" name="goodsName">
                        </div>
                        <div class="form-group">
                            <label>内部编号</label>
                            <input class="form-control" name="productID">
                        </div>
                        <div class="form-group">
                            <label>价格</label>
                            <input class="form-control" name="money">
                        </div>
                        <div class="form-group">
                            <label>货币单位</label>
                            <input class="form-control" name="moneyUnit">
                        </div>
                        <div class="form-group">
                            <label>游戏货币数量(钻石数量)</label>
                            <input class="form-control" name="diamond">
                        </div>
                        <div class="form-group">
                            <label>SDK对应的商品ID</label>
                            <input class="form-control" name="sdkGoodsID">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="subGoods" type="button" class="btn btn-primary"><i class="glyphicon glyphicon-floppy-disk"></i></button>
                    <button id="cancel" type="button" class="btn btn-danger"><i class="fa fa-reply fa-fw"></i></button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <!--增加商品面板END-->


    <!--商品面板-->
    <div class="modal fade" id="goodsWin" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">&times;
                    </button>
                    <h4 class="modal-title" id="goodsTitle">
                        管理商品
                    </h4>
                </div>
                <div class="modal-body">
                    <input class="form-control" id="goodsConfigID" type="hidden">
                    <div id="goodsTbar" class="btn-group">
                        <button type="button" class="btn btn-default" id="addGoods">
                            <i class="fa fa-plus fa-fw"></i>
                        </button>
                    </div>
                    <table id="goodsGrid" data-toggle="table"
                           data-url="<%=basePath%>/admin/goods/getAllGoods"
                           data-query-params="queryGoodsConfigParams"
                           data-pagination="true"
                           data-side-pagination="com.u8.server"
                           data-content-type="application/x-www-form-urlencoded"
                           data-method="post"
                           data-response-handler="responseHandler"
                           data-show-refresh="true"
                           data-show-toggle="true"
                           data-show-columns="true"
                           data-card-view="true"
                           data-toolbar="#goodsTbar"
                           data-height="450">
                        <thead>
                        <tr>
                            <th data-field="action" data-formatter="goodsFormatter" data-events="goodsEvents">操作</th>
                            <th data-field="goodsID" data-visible="false">ID</th>
                            <th data-field="goodsName">商品名称</th>
                            <th data-field="productID">商品编号</th>
                            <th data-field="money">价格</th>
                            <th data-field="diamond">钻石</th>
                            <th data-field="moneyUnit">货币单位</th>
                            <th data-field="sdkGoodsID">SDK 对应的商品ID</th>
                            <th data-field="channelID" data-visible="false">渠道ID</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <!--商品面板END-->

    <script>
        //全局Ajax事件
        $(document).ajaxComplete(function(event , xhr){
            var text = xhr.responseText;
            if(text.indexOf("<!DOCTYPE html>")>-1){
                window.location="<%=basePath%>/admin/login";
            }
            if(text.indexOf("操作失败")>-1){
                window.location="<%=basePath%>/admin/login";
            }
            if(xhr.status!=200){
                bootbox.alert('通信错误!<br>代码:'+xhr.status);
            }
        });
        $(document).ready(function () {
            $('#addBtn').click(function(){
                $('#editWindow').modal({backdrop: 'static', keyboard: false});
                $("#editTitle").html('添加商品配置');
                $("#id").val(null);
                $("#editForm")[0].reset();
            });
            /**
             * 增加按钮点击事件
             */
            $('#subBtn').click(function(){
                var tmp = $('#editForm').serialize().split("&");
                var values = {};
                for(var i in tmp){
                    var key = tmp[i].split("=")[0];
                    var value = decodeURIComponent(tmp[i].split("=")[1]);
                    console.debug(value);
                    values[key] = value;
                }
                $.ajax({
                    url:'<%=basePath%>/admin/goodsConfig/saveConfig',
                    data:values,
                    type:"POST",
                    dataType:'json',
                    success:function(data){
                        console.debug(data);
                        if(data.state==1){
                            $('#editWindow').modal("hide");
                            bootbox.alert(data.msg);
                        }
                        $("#tables").bootstrapTable('refresh');
                    }
                });
            });

            $("#addGoods").click(function(){
                $("#goodsWin").modal("hide");
                $("#addGoodsWin").modal({backdrop: 'static', keyboard: false});
            });

            $("#cancel").click(function(){
                $("#addGoodsWin").modal("hide");
                $("#goodsWin").modal({backdrop: 'static', keyboard: false});
            });

            $("#subGoods").click(function () {
                var tmp = $('#goodsForm').serialize().split("&");
                var values = {};
                for(var i in tmp){
                    var key = tmp[i].split("=")[0];
                    var value = decodeURIComponent(tmp[i].split("=")[1]);
                    values[key] = value;
                }
                values.goodsConfigID = $("#goodsConfigID").val();
                $.ajax({
                    url:'<%=basePath%>/admin/goods/saveGoods',
                    data:values,
                    type:"POST",
                    dataType:'json',
                    success:function(data){
                        if(data.state==1){
                            $("#addGoodsWin").modal("hide");
                            $("#goodsGrid").bootstrapTable('refresh');
                            $("#goodsWin").modal({backdrop: 'static', keyboard: false});
                        }else{
                            bootbox.alert(data.msg);
                        }
                    }
                });
            });

        });
        function queryParams(params) {
            var page = params.offset;
            page = (page)/params.limit+1;
            return {
                rows: params.limit,
                page: page
            };
        }

        function queryGoodsConfigParams(params){
            var page = params.offset;
            page = (page)/params.limit+1;
            return {
                rows: params.limit,
                page: page,
                goodsConfigID:Number($("#goodsConfigID").val())
            };
        }

        function responseHandler(res){
            return res;
        }

        function actionFormatter() {
            return [
                '<a class="edit ml10" href="javascript:void(0)" title="管理商品">',
                '<i class="fa fa-pencil fa-fw"></i>',
                '</a>',
                '<a class="remove ml10" href="javascript:void(0)" title="删除">',
                '<i class="fa fa-trash-o fa-fw"></i>',
                '</a>'
            ].join('');
        }
        window.actionEvents = {
            'click .edit': function (e, value, row) {
                $('#goodsWin').modal({backdrop: 'static', keyboard: false});
                $("#goodsConfigID").val(row.id);
                $("#goodsTitle").html("管理商品<small>["+row.name+"]</small>");
                $("#goodsAddTitle").html("添加商品<small>["+row.name+"]</small>");
                $("#goodsGrid").bootstrapTable('refresh');
            },
            'click .remove': function (e, value, row) {
                var id = row.id;
                bootbox.confirm("你确定要删除[商品:"+row.name+"]吗 , 此操作不可恢复", function(result){
                    if(result==true){
                        $.ajax({
                            url:'<%=basePath%>/admin/goodsConfig/removeConfig',
                            data:{
                                currConfigID:id
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
        function goodsFormatter() {
            return [
                '<a class="remove ml10" href="javascript:void(0)" title="删除">',
                '<i class="fa fa-trash-o fa-fw"></i>',
                '</a>'
            ].join('');
        }
        window.goodsEvents = {
            'click .remove':function(e, value, row){
                var id = row.goodsID;
                $.ajax({
                    url:'<%=basePath%>/admin/goods/removeGoods',
                    data:{
                        currGoodsID:id
                    },
                    type:"POST",
                    dataType:'json',
                    success:function(data){
                        if(data.state==1){
                            $("#goodsGrid").bootstrapTable('remove',{field:'goodsID' , values:[id]});
                        }else{
                            bootbox.alert(data.msg);
                        }
                    }
                });
            }
        }

    </script>
</body>

</html>
