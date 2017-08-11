<%@ page import="org.alan.asdk.utils.LanguageHelper" %>
<%@ page import="org.alan.asdk.utils.LanguageHelper" %><%--
  Created by IntelliJ IDEA.
  User: chenjie
  Date: 2014/12/9
  Time: 14:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String language = request.getParameter("language");
    LanguageHelper.INS.setLanguage(request);
    request.setAttribute("host",request.getServerName());
%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>TSDK</title>
    <!-- Bootstrap Core CSS -->
    <link href="<%=basePath%>/resources/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="<%=basePath%>/resources/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
    <!--bootbox JS-->
    <script src="<%=basePath%>/resources/js/bootbox.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/jquery.md5.js" type="text/javascript"></script>
    <script src="<%=basePath%>/resources/js/bootstrapValidator.min.js" type="text/javascript"></script>
    <script>
        (function ($) {
            $.getUrlParam = function (name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
                var r = window.location.search.substr(1).match(reg);
                if (r != null) return unescape(r[2]);
                return null;
            }
        })(jQuery);
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <form id="loginForm" class="form-horizontal" action="<%=basePath%>/account/login"
                  data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                  data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                  data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                <div class="form-group">
                    <div id="errorMsg" class="alert alert-info" hidden="hidden">
                    </div>
                </div>
                <div class="form-group">
                    <!--E-mail或用户名-->
                    <div class="col-xs-12">
                        <input type="text" class="form-control" name="username"
                               placeholder="${Login_text_1}"
                               data-bv-notempty="true"
                               data-bv-notempty-message="${Login_text_10}">
                    </div>
                </div>
                <div class="form-group">
                    <!--密码-->
                    <div class="col-xs-12">
                        <input type="password" class="form-control" name="psw"
                               placeholder="${Login_text_2}"
                               data-bv-notempty="true"
                               data-bv-notempty-message="${Login_text_3}">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="agree" checked="checked"/> ${Login_text_15}<a href="<%=basePath%>/account/protocol/${languageFileName}.jsp">[${Login_text_16}]</a>
                        </label>
                    </div>
                    </div>
                </div>
                <!--登录-->
                <button id="submit" type="submit" class="btn btn-info btn-block">${Login_text_4}</button>
                <div class="form-group mg-t" id="buttons">
                    <!--注册-->
                    <div class="col-xs-3 text-center"><a href="<%=basePath%>/account/register.jsp?language=<%=language%>">${Login_text_5}</a>
                    </div>
                    <!--游客登录-->
                    <div class="col-xs-3 text-center"><span id="guest" style="color: #337AB7;">${Login_text_6}</span></div>
                    <!--忘记密码-->
                    <div class="col-xs-3 text-center"><a
                            href="<%=basePath%>/account/identify-account.jsp?language=<%=language%>">${Login_text_7}</a></div>
                    <div class="col-xs-3 text-center"><a
                            href="<%=basePath%>/account/modify-pwd.jsp?language=<%=language%>">${Login_text_11}</a></div>
                </div>
            </form>
            <script>
            <c:if test="${host=='localhost'}">
                var tsixiLoginInterface = {
                    getLoginInfo: function () {
                        return "{\"channel\":\"1\",\"appID\":\"1\",\"deviceName\":\"test\"}";
                    },
                    onLoginFromJavaScript: function (UID, userName, pwd, session) {
                        alert("登录成功");
                        alert(UID);
                    }
                };
            </c:if>

                var _pwd;
                var autoLogin;
                var isAgree = true;
                var onReady =  function() {
                    if ($.getUrlParam("username")) {
                        $("[name=username]").val($.getUrlParam("username"));
                    }
                    if ($.getUrlParam("psw")) {
                        $("[name=psw]").val($.getUrlParam("psw"));
                    }
                    if ($.getUrlParam("psw") && $.getUrlParam("username")) {
                        return;
                    }
                    //noinspection JSUnresolvedVariable,JSUnresolvedFunction
                    var userInfo = tsixiLoginInterface.getLoginInfo();
                    userInfo = $.parseJSON(userInfo);
                    if (userInfo.userName) {
                        $("[name=username]").val(userInfo.userName);
                    }
                    if (userInfo.pwd) {
                        $("[name=psw]").val(userInfo.pwd);
                        _pwd = userInfo.pwd;
                    }
                    if (userInfo.userName && userInfo.pwd) {
                        var count = 3;
                        $("#buttons").hide();
                        autoLogin = setInterval(function () {
                            var msg = $("#errorMsg");
                            msg.show(100);
                            disabledFields(true);
                            //秒后,将自动登录!
                            msg.html('<p>' + count + '${Login_text_8} </p><button type="button" class="btn btn-default" onclick="cancel()">${Login_text_9}</button>');//取消
                            if (count == 0) {
                                clearInterval(autoLogin);
                                $("#errorMsg").hide(100);
                                $("#buttons").show();
                                login();
                            }
                            count--;
                        }, 1000);
                    }
                };
                $("#loginForm").bootstrapValidator({
                    fields: {
                        'agree': {
                            validators: {
                                notEmpty: {
                                    message: '${Login_text_14}'//你必须同意用户许可协议才能开始游戏
                                }
                            }
                        }
                    },
                    submitHandler: function () {
                        $("#errorMsg").hide(200);
                        login();
                    }
                });
                $(document).ready(function () {
                    $("input").change(function () {
                        $("#errorMsg").hide(200);
                    });
                    $("#guest").click(function () {
                        clearInterval(autoLogin);
                        _pwd = null;
                        $.ajax({
                            url: '<%=basePath%>/account/guest',
                            dataType: 'json',
                            success: function (data) {
                                $("[name=username]").val(data.data.guestName);
                                $("[name=psw]").val(data.data.psw);
                                login();
                            }
                        });
                    });
                <c:if test="${host=='localhost'}">
                   onReady();
                </c:if>
                });
                function cancel() {
                    clearInterval(autoLogin);
                    _pwd = null;
                    disabledFields(false);
                    $("[name=psw]").val("");
                    $("#errorMsg").hide(100);
                    $("#buttons").show();
                }
                function disabledFields(flag) {
                    $("[name=username]").attr('disabled', flag);
                    $("[name=psw]").attr('disabled', flag);
                    $("#submit").attr('disabled', flag);
                }
                function login() {
                    //noinspection JSUnresolvedVariable
                    var submit = $("[type=submit]");
                    submit.html("<span class='fa fa-circle-o-notch fa-spin fa-2x fa-fw margin-bottom'></span>").attr('disabled', true);
                    var psw = $("[name=psw]");
                    if (!_pwd && psw.val().length != 32) {
                        psw.val($.md5(psw.val()));
                    }
                    //noinspection JSUnresolvedFunction,JSUnresolvedVariable
                    var userInfo = tsixiLoginInterface.getLoginInfo();
                    userInfo = $.parseJSON(userInfo);
                    var form = $("#loginForm");
                    //noinspection JSUnresolvedVariable
                    var deviceInfo = "";
                    if(userInfo.deviceName){
                        deviceInfo += "deviceName = "+userInfo.deviceName +","
                    }
                    if(userInfo.deviceVersion){
                        deviceInfo +="deviceVersion = "+ userInfo.deviceVersion
                    }
                    if(userInfo.deviceId){
                        deviceInfo +="deviceId = "+ userInfo.deviceId
                    }
                    var params = "username=" + $("[name=username]").val() + "&psw=" + psw.val() + "&appID=" + userInfo.appID + "&channelID=" + userInfo.channel
                            +"&deviceInfo="+deviceInfo+"language="+userInfo.language;
                    $.post(form.attr('action'), params, function (result) {
                        if (result.state == 1) {
                            //noinspection JSUnresolvedVariable
                            var data = {
                                UID: result.data.userID,
                                userName: $("[name=username]").val(),
                                pwd: psw.val(),
                                session: result.data.token
                            };
                            //noinspection JSUnresolvedFunction,JSUnresolvedVariable
                            tsixiLoginInterface.onLoginFromJavaScript(JSON.stringify(data));
                        } else {
                            //noinspection JSUnresolvedVariable
                            $("#errorMsg").html(result.des).show(200);
                            //登录
                            submit.html("${Login_text_4}").attr('disabled', false);
                        }
                    }, 'json');
                }
            </script>
        </div>
        <div class="col-md-3"></div>
    </div>
</div>
</body>
</html>
