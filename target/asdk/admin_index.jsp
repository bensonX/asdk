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
    <title>A-SDK</title>
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
            <a class="navbar-brand" href="<%=basePath%>/admin/index">A-SDK</a>
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
</div>
</body>
</html>
