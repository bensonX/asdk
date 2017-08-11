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

    <title>ASDK - admin管理</title>
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
                <h1 class="page-header">用户管理</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div id="toolbar" class="btn-group">
                    <button type="button" class="btn btn-default" id="addBtn">
                        <i class="fa fa-plus fa-fw"></i>
                    </button>
                </div>
                <script>
                    $('#addBtn').click(function () {
                        $('#editWindow').modal({backdrop: 'static', keyboard: false});
                        $("#editTitle").html('添加用户');
                        $("#id").val(null);
                        $("#editForm")[0].reset();
                    });
                </script>
                <table id="tables" data-toggle="table"
                       data-url="<%=basePath%>/admin/manage/getAdmins"
                       data-content-type="application/x-www-form-urlencoded"
                       data-method="post"
                       data-show-refresh="true"
                       data-show-toggle="true"
                       data-show-columns="true"
                       data-toolbar="#toolbar"
                       data-height="600">
                    <thead>
                    <tr>
                        <th data-field="action" data-formatter="actionFormatter" data-events="actionEvents">操作</th>
                        <th data-field="id">用户ID</th>
                        <th data-field="username">用户名</th>
                        <th data-field="fullName">用户全名</th>
                        <th data-field="permission" data-formatter="permissionFormat">权限</th>
                        <th data-field="pAdminName">上级用户</th>
                        <th data-field="pAdminID" data-visible="false">上级用户</th>
                    </tr>
                    </thead>
                </table>
                <script>
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
                    function permissionFormat(value) {
                        switch (value) {
                            case  0 :
                                return "开发者";
                            case  1 :
                                return "管理员";
                            case  2 :
                                return "操作员";
                            case  3 :
                                return "客服员";
                        }
                    }
                    window.actionEvents = {
                        'click .edit': function (e, values, row) {
                            $('#editWindow').modal({backdrop: 'static', keyboard: false});
                            $("#editTitle").html('修改模块');
                            $("#editForm")[0].reset();
                            var inputs = $("input");
                            for (var i in inputs) {
                                var value = row[inputs[i].name];
                                if (value != undefined) {
                                    $(inputs[i]).val(value);
                                }
                            }
                            $('#permission').select2().val(row.permission);
                            $('#permission').trigger('change');
                            $('#pAdminID').append(new Option(row.pAdminName, row.pAdminID, true));
                            $('#pAdminID').trigger('change');
                        },
                        'click .remove': function (e, value, row) {
                            bootbox.confirm("你确定要删除[用户:" + row.fullName + "]吗 , 此操作不可恢复", function (result) {
                                if (result == true) {
                                    $.ajax({
                                        url: '<%=basePath%>/admin/manage/removeAdmin',
                                        data: {
                                            id: row.id
                                        },
                                        type: "POST",
                                        dataType: 'json',
                                        success: function (data) {
                                            if (data.state == 1) {
                                                $("#tables").bootstrapTable('remove', {field: 'id', values: [row.id]});
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    };
                </script>
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
                    添加模块
                </h4>
            </div>
            <div class="modal-body">
                <form id="editForm">
                    <input class="form-control" name="id" type="hidden" id="id">
                    <input class="form-control" name="pAdminID" type="hidden">
                    <div class="form-group">
                        <label>用户名</label>
                        <input type="text" class="form-control" name="username">
                    </div>
                    <div class="form-group">
                        <label>用户密码</label>
                        <input type="text" class="form-control" name="password">
                        <p class="help-block">密码只能重新设置</p>
                    </div>
                    <div class="form-group">
                        <label>全名</label>
                        <input type="text" class="form-control" name="fullName">
                    </div>
                    <div class="form-group">
                        <label>上级用户</label>
                        <select class="js-example-basic-hide-search" id="pAdminID" style="width: 100%" name="pAdminID">
                        </select>
                        <p class="help-block"> 上级用户代表谁有权限操作该用户 , 权限是递归的 (如 A是B的上级用户 B是C的上级用户 则 A可以操作C)</p>
                        <script>
                            $('#pAdminID').select2({
                                minimumResultsForSearch: Infinity,
                                ajax: {
                                    url: '<%=basePath%>/admin/manage/getAdmins',
                                    dataType: 'json',
                                    delay: 250,
                                    processResults: function (data) {
                                        var results = [];
                                        results.push({
                                            id:${admin.id},
                                            text: '${admin.fullName}'
                                        });
                                        for (var i in data) {
                                            results.push({
                                                id: data[i].id,
                                                text: data[i].fullName
                                            });
                                        }
                                        return {
                                            results: results
                                        };
                                    },
                                    cache: true
                                }
                            });
                        </script>
                    </div>
                    <div class="form-group">
                        <label>用户组</label>
                        <select class="js-example-basic-hide-search" id="permission" style="width: 100%"
                                name="permission">
                            <option value="0">开发者</option>
                            <option value="1">管理员</option>
                            <option value="2">操作员</option>
                            <option value="3">客服员</option>
                        </select>
                        <p class="help-block"> 权限级别: 开发者 > 管理员 > 操作员 > 客服员</p>
                        <script>
                            $('#permission').select2({
                                minimumResultsForSearch: Infinity
                            });
                        </script>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="subBtn" type="button" class="btn btn-primary"><i
                        class="glyphicon glyphicon-floppy-disk"></i></button>
                <script>
                    $('#subBtn').click(function () {
                        var tmp = $('#editForm').serialize().split("&");
                        var values = {};
                        for (var i in tmp) {
                            var key = tmp[i].split("=")[0];
                            var value = decodeURIComponent(tmp[i].split("=")[1]);
                            values[key] = value;
                        }
                        if (values.password != "******") {
                            values.password = $.md5(values.password);
                        } else {
                            values.password = "";
                        }
                        $.ajax({
                            url: '<%=basePath%>/admin/manage/saveAdmin',
                            data: values,
                            type: "POST",
                            dataType: 'json',
                            success: function (data) {
                                if (data.state == 1) {
                                    $('#editWindow').modal("hide");
                                    bootbox.alert(data.msg);
                                } else {
                                    bootbox.alert(data.msg);
                                }
                                $("#tables").bootstrapTable('refresh');
                            }
                        });
                    });
                </script>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<!-- 增加END -->
<script>
    //全局Ajax事件
    $(document).ajaxComplete(function (event, xhr) {
        var text = xhr.responseText;
        if (text.indexOf("<!DOCTYPE html>") > -1) {
            window.location = "<%=basePath%>/admin/login";
        }
        if (xhr.status != 200) {
            bootbox.alert('通信错误!<br>代码:' + xhr.status);
        }
    });
</script>

</body>

</html>
