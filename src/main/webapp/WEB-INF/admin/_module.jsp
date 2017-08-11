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

    <title>ASDK - 模块管理</title>
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
    <!--tree view-->
    <link href="<%=basePath%>/resources/css/bootstrap-treeview.min.css" rel="stylesheet"/>
    <!--jquery tree-->
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
    <script src="<%=basePath%>/resources/js/bootstrap-treeview.min.js" type="text/javascript"></script>
    <!--jquery tree grid-->
    <script src="<%=basePath%>/resources/js/app/moduleManage/moduleManage.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/bootstrapValidator.min.js" type="text/javascript"></script>
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
                <h1 class="page-header">模块管理</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 mg-b">
                <div class="btn-group">
                    <button type="button" class="btn btn-default" id="add"><span class="fa fa-plus"></span></button>
                    <button type="button" class="btn btn-default" id="edit"><span class="fa fa-edit"></span></button>
                    <button type="button" class="btn btn-default" id="remove"><span class="fa fa-trash"></span></button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div id="tree"></div>
            </div>
            <!-- /.col-lg-12 -->
            <script>

            </script>
        </div>

    </div>
    <!-- /#page-wrapper -->
</div>
<!-- /#wrapper -->
<!-- 增加渠道面板 -->
<div class="modal fade" id="editWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form id="editForm" action="<%=basePath%>/admin/module/saveModule">
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
                    <input class="form-control" name="id" type="hidden" id="id">
                    <div class="form-group">
                        <label>模块名称</label>
                        <input type="text" class="form-control" name="name">
                    </div>
                    <div class="form-group">
                        <label>父节点</label>
                        <select class="js-example-basic-hide-search" name="pid" id="pid" style="width: 100%"></select>
                    </div>
                    <div class="form-group">
                        <label>是否为叶子节点</label>
                        <select class="js-example-basic-hide-search" name="leaf" id="leaf" style="width: 100%">
                            <option value="true">是</option>
                            <option value="false">否</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>页面地址</label>
                        <input type="text" class="form-control" name="url" id="url">
                        <p class="help-block">该模块的主要显示页面</p>
                    </div>
                    <div class="form-group">
                        <label>图标</label>
                        <input type="text" class="form-control" name="iconCls">
                    </div>
                    <div class="form-group">
                        <label>最低权限</label>
                        <select class="js-example-basic-hide-search" id="permission" style="width: 100%"
                                name="permission">
                            <option value="0">开发者</option>
                            <option value="1">管理员</option>
                            <option value="2">操作员</option>
                            <option value="3">客服员</option>
                        </select>
                        <p class="help-block">访问这个模块的最低权限,低于此权限的用户将不可见 权限级别: 开发者 > 管理员 > 操作员 > 客服员</p>
                        <script>
                            $('#permission').select2({
                                minimumResultsForSearch: Infinity
                            });
                            $('#leaf').select2({
                                minimumResultsForSearch: Infinity
                            }).on('select2:select', function (evt) {
                                var value = evt.params.data.id;
                                $("#url").attr("disabled",value=="false");
                            });
                            $('#pid').select2({
                                minimumResultsForSearch: Infinity,
                                ajax: {
                                    url: '<%=basePath%>/admin/module/getSimplePModules',
                                    dataType: 'json',
                                    delay: 250,
                                    processResults: function (data) {
                                        data.push({
                                            id:-1,
                                            text:"根节点"
                                        });
                                        return {
                                            results: data
                                        };
                                    },
                                    cache: true
                                }
                            });
                        </script>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="subBtn" type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-floppy-disk"></i></button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </form>
</div>
<!-- 增加END -->
<script>
    //全局Ajax事件
    $(document).ajaxComplete(function (event, xhr) {
        var text = xhr.responseText;
        if (text.indexOf("<!DOCTYPE html>") > -1) {
            window.location = "<%=basePath%>/admin/login";
        }
        if (text.indexOf("操作失败") > -1) {
            window.location = "<%=basePath%>/admin/login";
        }
        if (xhr.status != 200) {
            bootbox.alert('通信错误!<br>代码:' + xhr.status);
        }
    });
</script>
</body>

</html>
