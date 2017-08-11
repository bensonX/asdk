<%@ page import="com.u8.server.utils.LanguageHelper" %>
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
    //重置密码
    LanguageHelper.INS.setLanguage(request);
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
</head>
<body>
    <div class="container">
        <div class="row mg-t">
            <div class="col-md-3"></div>
            <div class="col-md-6">
                <div id="errorMsg" class="alert alert-danger" hidden="hidden"></div>
                <form id="loginForm" class="form-horizontal" action="<%=basePath%>/account/resetPsw"
                      data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                      data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                      data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                    <div class="form-group">
                        <div class="col-xs-8">
                            <%--E-mail不能为空--%>
                            <%--E-mail格式错误--%>
                            <input type="email" class="form-control" name="email"
                                   placeholder="E-mail"
                                   data-bv-notempty="true"
                                   data-bv-notempty-message="${identify_text_1}"
                                   data-bv-emailaddress="true"
                                   data-bv-emailaddress-message="${identify_text_2}">
                        </div>
                        <div class="col-xs-2">
                            <%--获取验证码--%>
                            <button class="btn btn-info" type="button" id="getMailCaptchaBtn">${identify_text_3}</button>
                        </div>
                    </div>
                    <script>
                        $("#getMailCaptchaBtn").click(function (e) {
                            $("#errorMsg").hide(200);
                            var me = $(e.target);
                            $.ajax({
                                url:'<%=basePath%>/account/sendResetMailCaptcha',
                                data:{
                                    email:$("[name=email]").val()
                                },
                                dataType:'json',
                                success:function(result){
                                    var i = 60;
                                    me.html("60${identify_text_4}").attr('disabled',true);//秒后重新获取
                                   var t =  setInterval(function(){
                                        if(i<1){
                                            me.html("${identify_text_5}").attr('disabled',false);//重新获取
                                            clearInterval(t);
                                            return;
                                        }
                                        i -- ;
                                        me.html(i+"${identify_text_4}");//秒后重新获取
                                    },1000);
                                    if(result.state!=1){
                                        $("#errorMsg").html('<span class="fa fa-times-circle" style="margin-right: 10px"></span>'+result.des).show(200);
                                    }
                                }
                            });
                        });
                    </script>
                    <div class="form-group">
                        <div class="col-xs-8">
                            <%--请输入邮箱收到的4位数字--%>
                            <%--验证码不能为空--%>
                            <input type="text" class="form-control" name="mailCaptcha"
                                   placeholder="${identify_text_7}"
                                   data-bv-notempty="true"
                                   data-bv-notempty-message="${identify_text_8}">
                        </div>
                        <div class="col-xs-2">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-8">
                            <%--密码--%>
                            <%--密码不能为空--%>
                            <input type="password" class="form-control" name="psw"
                                   placeholder="${identify_text_9}"
                                   data-bv-notempty="true"
                                   data-bv-notempty-message="${identify_text_10}">
                        </div>
                        <div class="col-xs-2">
                        </div>
                    </div>
                    <div class="form-group">
                        <%--请再输入一次密码--%>
                        <%--密码不能为空--%>
                        <div class="col-xs-8">
                            <input type="password" class="form-control" name="conform"
                                   placeholder="${identify_text_12}"
                                   data-bv-notempty="true"
                                   data-bv-notempty-message="${identify_text_13}">
                        </div>
                        <div class="col-xs-2">
                        </div>
                    </div>
                    <%--输入右侧字符--%>
                    <%--该项不能为空--%>
                    <div class="form-group">
                        <div class="col-xs-6">
                            <input type="text" class="form-control" name="captcha"
                                   placeholder="${identify_text_14}"
                                   data-bv-notempty="true"
                                   data-bv-notempty-message="${identify_text_15}">
                        </div>
                        <label class="col-xs-3"><img src="<%=basePath%>/security/getCaptcha" id="captcha" height="35px"></label>
                        <div class="col-xs-1">
                            <button class="btn btn-info" type="button" id="refresh"><span class="glyphicon glyphicon-refresh"></span></button>
                        </div>
                    </div>
                    <script>
                        $("#refresh").click(function () {
                            $("#captcha").attr("src","<%=basePath%>/security/getCaptcha?id="+Math.random());
                        });
                        $("#captcha").click(function (e) {
                            $(e.target).attr("src","<%=basePath%>/security/getCaptcha?id="+Math.random());
                        });
                    </script>
                    <%--提交--%>
                    <button type="submit" class="btn btn-info btn-block">${identify_text_16}</button>
                    <%--返回--%>
                    <a class="btn btn-info btn-block" href="<%=basePath%>/index.jsp?language=<%=language%>">${identify_text_18}</a>
                </form>
                <script>
                    $("#loginForm").bootstrapValidator({
                        fields: {
                            psw: {
                                validators: {
                                    stringLength: {
                                        min: 6,
                                        max:12,
                                        message: '${identify_text_19}'//密码长度应介于6位到12位之间!
                                    },
                                    identical: {
                                        field: 'conform',
                                        message: '${identify_text_20}'//两次输入的密码不一致
                                    },
                                    regexp: {
                                        regexp: /((?=.*\d)(?=.*[a-z]))/,
                                        message: '${identify_text_21}'//密码至少包含一个英文字母和一个数字
                                    }
                                }
                            },
                            mailCaptcha:{
                                validators: {
                                    between: {
                                        min: 1000,
                                        max: 10000,
                                        message: '${identify_text_22}'//验证码格式错误
                                    }
                                }
                            },
                            conform:{
                                validators: {
                                    identical: {
                                        field: 'psw',
                                        message: '${identify_text_20}'//两次输入的密码不一致
                                    }
                                }
                            }
                        },
                        submitHandler: function(validator, form) {
                            $("#errorMsg").hide(200);
                            var submit = $("[type=submit]");
                            submit.html("<span class='fa fa-circle-o-notch fa-spin fa-2x fa-fw margin-bottom'></span>").attr('disabled',true);
                            //noinspection JSUnresolvedVariable
                            // Use Ajax to submit form data
                            $.post(form.attr('action'), form.serialize(), function(result) {
                                submit.html("${identify_text_16}").attr('disabled',false);//提交
                                if(result.state!=1){
                                    //noinspection JSUnresolvedVariable
                                    var msg = result.des;
                                    $("#errorMsg").html('<span class="fa fa-times-circle" style="margin-right: 10px"></span>'+ msg).show(200);
                                    if(result.state==8){//用户名密码格式错误
                                        validator.updateElementStatus($("[name=username]"),"INVALID",msg)
                                    }
                                    if(result.state==7||result.state==3){//验证码已超时
                                        validator.updateElementStatus($("[name=mailCaptcha]"),"INVALID",msg)
                                    }
                                    return;
                                }
                                window.location="success.jsp"
                            }, 'json');

                        }
                    });
                </script>
            </div>
            <div class="col-md-3"></div>
        </div>
    </div>
</body>
</html>
