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

    <title>ASDK - 自渠道功能配置</title>
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
    <script src="<%=basePath%>/resources/js/bootstrapValidator.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/app/SelfChannel.js" type="text/javascript"></script>
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
                    <h1 class="page-header">自渠道功能配置</h1>
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
                           data-url="<%=basePath%>/admin/self/getAllChannels"
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
                            <th data-field="configID">配置ID</th>
                            <th data-field="channelID">渠道ID</th>
                            <th data-field="language" >默认语言</th>
                            <th data-field="openLogin" >开启登录</th>
                            <th data-field="openRegister">开启注册</th>
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
        <form action="<%=basePath%>/admin/self/saveChannel" id="editForm">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">&times;
                        </button>
                        <h4 class="modal-title" id="editTitle">
                            添加配置
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input class="form-control" name="id" type="hidden" id="id" value="0">
                        <div class="row">
                            <input type="hidden" name="configID">
                            <div class="col-lg-12"><div class="form-group">
                                <label>渠道</label>
                                <select class="js-example-basic-hide-search" id="channelID" style="width: 100%" name="channelID"></select>
                            </div></div>
                            <script>
                                $('#channelID').select2({
                                    minimumResultsForSearch: Infinity,
                                    ajax:{
                                        url:'<%=basePath%>/admin/channels/getSimpleChannels',
                                        dataType:'json',
                                        delay: 250,
                                        processResults: function (data) {
                                            var results = [];
                                            for(var i in data){
                                                results.push({
                                                    id:data[i].channelID,
                                                    text:data[i].channelName
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
                            <div class="col-lg-12"><div class="form-group">
                                <label>默认语言</label>
                                <select class="js-example-basic-hide-search" id="language" style="width: 100%" name="language">
                                    <option value="0">中文</option>
                                    <option value="1">泰语</option>
                                    <option value="2">越南语</option>
                                </select>
                            </div></div>
                            <script>
                                $('#language').select2({
                                    minimumResultsForSearch: Infinity
                                });
                            </script>
                            <div class="col-lg-12"><div class="form-group">
                                <label>登录</label>
                                <select class="js-example-basic-hide-search" id="openLogin" style="width: 100%" name="openLogin">
                                    <option value="true">开启</option>
                                    <option value="false">关闭</option>
                                </select>
                            </div></div>
                            <script>
                                $('#openLogin').select2({
                                    minimumResultsForSearch: Infinity
                                });
                            </script>
                            <div class="col-lg-12"><div class="form-group">
                                <label>注册</label>
                                <select class="js-example-basic-hide-search" id="openRegister" style="width: 100%" name="openRegister">
                                    <option value="true">开启</option>
                                    <option value="false">关闭</option>
                                </select>
                            </div></div>
                            <script>
                                $('#openRegister').select2({
                                    minimumResultsForSearch: Infinity
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
