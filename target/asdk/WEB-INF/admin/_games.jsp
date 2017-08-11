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

    <title>ASDK - 游戏管理</title>
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
                    <h1 class="page-header">游戏管理</h1>
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
                           data-url="<%=basePath%>/admin/games/getAllGames"
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
                            <th data-field="appID">appID</th>
                            <th data-field="name">游戏名称</th>
                            <th data-field="appkey">App Key</th>
                            <th data-field="appRSAPubKey" data-visible="false" >Pay Public Key</th>
                            <th data-field="appRSAPriKey" data-visible="false">Pay Private Key</th>
                            <th data-field="payCallback">支付回调地址</th>
                            <th data-field="payCallbackDebug" >支付回调调试地址</th>
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

    <!-- 增加面板 -->
    <div class="modal fade" id="editWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">&times;
                    </button>
                    <h4 class="modal-title" id="editTitle">
                        添加游戏
                    </h4>
                </div>
                <div class="modal-body">
                    <form id="editForm">
                        <input class="form-control" name="appID" type="hidden" id="id">
                        <div class="row">
                            <div class="col-lg-12"><div class="form-group">
                                <label>游戏名称</label>
                                <input class="form-control" name="name">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>支付回调地址</label>
                                <input class="form-control" name="payCallback">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>支付回调调试地址</label>
                                <input class="form-control" name="payCallbackDebug">
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
                $("#editTitle").html('添加游戏');
                $("#id").val(null);
                $("#editForm")[0].reset();
            });
            $('#subBtn').click(function(){
                var tmp = $('#editForm').serialize().split("&");
                var values = {};
                for(var i in tmp){
                    var key = tmp[i].split("=")[0];
                    var value = decodeURIComponent(tmp[i].split("=")[1]);
                    values[key] = value;
                }
                $.ajax({
                    url:'<%=basePath%>/admin/games/saveGame',
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

        });
        function queryParams(params) {
            var page = params.offset;
            page = (page)/params.limit+1;
            return {
                rows: params.limit,
                page: page
            };
        }
        function responseHandler(res){
            return res;
        }

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
        window.actionEvents = {
            'click .edit': function (e, value, row, index) {
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
            },
            'click .remove': function (e, value, row) {
                var id = row.appID;
                bootbox.confirm("你确定要删除[游戏:"+row.name+"]吗 , 此操作不可恢复", function(result){
                    if(result==true){
                        $.ajax({
                            url:'<%=basePath%>/admin/games/removeGame',
                            data:{
                                currAppID:id
                            },
                            type:"POST",
                            dataType:'json',
                            success:function(data){
                                bootbox.alert(data.msg);
                                if(data.state==1){
                                    $("#tables").bootstrapTable('remove',{field:'appID' , values:[id]});
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
