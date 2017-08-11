<!DOCTYPE html>
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

    <title>ASDK - Aaln Software Development Kit</title>
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

    <div class="container">
        <div class="row " >
            <div class="col-md-4 col-md-offset-4 absolute-center">
                <h1 style="color: #69AA46;text-align: center;margin-bottom: 100px">ASDK <small style="color: #DD5A43">Manage</small></h1>
                <form role="form">
                    <fieldset>
                        <div class="form-group">
                            <input class="form-control" id="username" name="username" type="text" placeholder="用户名" autofocus>
                        </div>
                        <div class="form-group">
                            <input class="form-control" id="pwd" placeholder="密码" name="password" type="password" value="">
                        </div>
                        <div id="errorMsg" class="alert alert-danger"><span class="fa fa-times-circle" style="margin-right: 10px"></span>登录失败! 用户名或密码错误!</div>
                        <!-- Change this to a button or input when using this as a form -->
                        <a class="btn btn-lg btn-success btn-block" value="登录" onclick="login();" id="loginBtn">登录</a>
                    </fieldset>
                </form>
            </div>
        </div>
        <nav class="navbar-fixed-bottom">
            <div class="navbar-inner navbar-content-center" style="padding: 10px; color: #FFF;text-align: center">
                <p class="text-muted credit" id="com"></p>
                <p class="text-muted credit"><a href="http://www.tsixi.com" target="_blank">www.Tsixi.com</a></p>
            </div>
        </nav>
    </div>
    <script>
        $(document).ready(function(){
            $("#errorMsg").hide();
            var year = new Date().getFullYear();
            $("#com").html("&copy; "+year+" Chengdu Tsixi Technology Co., Ltd.");
            $("#username").keydown(function(){
                $("#errorMsg").hide(200);
            });
            $("#pwd").keydown(function(){
                $("#errorMsg").hide(200);
            });
        });
        $(document).keydown(function(event){
           if(event.keyCode == 13){
               login();
           }
        });
        function login(){
            var username = $("#username").val();
            var pwd = $("#pwd").val();
            pwd = $.md5(pwd);
            $("#loginBtn").html("<span class='fa fa-spinner fa-spin'></span>");
            $("#loginBtn").attr("disabled",true);
            setTimeout(function(){
                $.post('<%=basePath%>/admin/doLogin', {username: username, password: pwd}, function (result) {
                    if (result.state == 1) {
                        setTimeout(function(){
                            $("#loginBtn").attr("disabled",false);
                            $("#loginBtn").html("登录");
                            location.href = "<%=basePath%>/admin_index.jsp"
                        },200)
                    }else if(result.state == 13){
                        $("#loginBtn").removeClass().addClass("btn btn-lg btn-danger btn-block");
                        $("#loginBtn").attr("disabled","disabled");
                        $("#loginBtn").html("登录被拒绝!请稍后再试");
                    } else {
                        $("#loginBtn").attr("disabled",false);
                        $("#loginBtn").html("登录");
                        $("#errorMsg").show(200);
                    }
                }, 'json');
            },500);
        }
    </script>

</body>

</html>
