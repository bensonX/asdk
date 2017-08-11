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

    <title>ASDK - 充值渠道商管理</title>
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
    <!-- Select2 JS-->
    <script src="<%=basePath%>/resources/js/select2.full.min.js" type="text/javascript" ></script>
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
    <script src="<%=basePath%>/resources/js/app/ChargeChannelManage.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/bootstrapValidator.min.js" type="text/javascript"></script>
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
                    <h1 class="page-header">充值渠道商管理</h1>
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
                           data-url="<%=basePath%>/admin/chargeChannel/getAllChannels"
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
                            <th data-field="id">渠道ID</th>
                            <th data-field="chargeChannelName">渠道名称</th>
                            <th data-field="iconUrl" >图标</th>
                            <th data-field="scriptClass" >脚本类路径</th>
                            <th data-field="url">充值链接地址</th>
                            <th data-field="adminName">所有者</th>
                            <th data-field="appID" data-visible="false">APP ID</th>
                            <th data-field="appKey" data-visible="false" >APP Key</th>
                            <th data-field="appSecret" data-visible="false">APP Secret</th>
                            <th data-field="config" data-visible = "false">Config</th>
                            <th data-field="adminID" data-visible = "false">Admin ID</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
    </div>
    <!-- /#wrapper -->

    <!-- 增加面板 -->
    <div class="modal fade" id="editWindow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <form action="<%=basePath%>/admin/chargeChannel/saveChannel" id="editForm">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">&times;
                        </button>
                        <h4 class="modal-title" id="editTitle">
                            添加充值渠道商
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input class="form-control" name="id" type="hidden" id="id" value="0">
                        <div class="row">
                            <div class="col-lg-12"><div class="form-group">
                                <label>充值渠道商名称</label>
                                <input class="form-control" name="chargeChannelName">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>APP ID</label>
                                <input class="form-control" name="appID">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>APP Key</label>
                                <input class="form-control" name="appKey">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>APP Secret</label>
                                <input class="form-control" name="appSecret">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>其他参数</label>
                                <input class="form-control" name="config">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>图标地址</label>
                                <input class="form-control" name="iconUrl">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>充值链接地址</label>
                                <input class="form-control" name="url">
                            </div></div>
                            <div class="col-lg-12"><div class="form-group">
                                <label>所有者 :</label>
                                <select class="js-example-basic-hide-search" id="adminID" style="width: 100%" name="adminID"></select>
                                <p class="help-block">完全公开的代表所有用户可见 , 分配给谁代表该用户及其子级用户可见</p>
                            </div></div>
                            <script>
                                $('#adminID').select2({
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
</body>

</html>
