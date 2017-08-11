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
    //绑定邮箱
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
    <div class="row mg-t">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <form id="form" class="form-horizontal" action="<%=basePath%>/account/register"
                  data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                  data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                  data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                <div class="form-group">
                    <div id="errorMsg" class="alert alert-danger" hidden="hidden">
                        <span class="fa fa-times-circle" style="margin-right: 10px"></span>
                    </div>
                </div>
                <!--用户名-->
                <div class="form-group">
                    <div class="col-xs-12">
                    <input type="text" class="form-control" name="username"
                           placeholder="${register_text_1}"
                           data-bv-notempty="true"
                           data-bv-notempty-message="${register_text_2}">
                    </div>
                </div>
                <!--E-mail-->
                <div class="form-group">
                    <div class="col-xs-12">
                    <input type="email" class="form-control" name="email"
                           placeholder="${register_text_3}"
                           data-bv-emailaddress="true"
                           data-bv-emailaddress-message="${register_text_4}"
                          >
                    </div>
                </div>
                <!--密码-->
                <div class="form-group">
                    <div class="col-xs-12">
                    <input type="password" class="form-control" name="psw"
                           placeholder="${register_text_6}"
                           data-bv-notempty="true"
                           data-bv-notempty-message="${register_text_7}">
                    </div>
                </div>
                <!--确认密码-->
                <div class="form-group">
                    <div class="col-xs-12">
                    <input type="password" class="form-control" name="conform"
                           placeholder="${register_text_8}"
                           data-bv-notempty="true"
                           data-bv-notempty-message="${register_text_9}">
                    </div>
                </div>
                <!--注册-->
                    <button type="submit" class="btn btn-info btn-block">${register_text_12}</button>
                <!--返回-->
                <a href="<%=basePath%>/index.jsp?language=<%=language%>" class="btn btn-info btn-block">${register_text_14}</a>
            </form>
            <script>
                $(document).ready(function () {
                    $("[name=username]").val("");
                    $("[name=psw]").val("");
                    $("[name=conform]").val("");
                    $("[name=email]").val("");
                });
                $("#form").bootstrapValidator({
                    fields: {
                        username: {
                            validators: {
                                stringLength: {
                                    min: 6,
                                    max: 12,
                                    message: '${register_text_15}'//用户名长度应介于6位到12位之间!
                                },
                                regexp: {
                                    regexp: /((?=.*\d)(?=.*[a-z]))/,
                                    message: '${register_text_16}'//用户名至少包含一个英文字母
                                }
                            }
                        },
                        psw: {
                            validators: {
                                stringLength: {
                                    min: 6,
                                    max: 12,
                                    message: '${register_text_10}!'// 密码长度应介于6位到12位之间!
                                },
                                identical: {
                                    field: 'conform',
                                    message: '${register_text_11}'//两次输入的密码不一致
                                },
                                regexp: {
                                    regexp: /((?=.*\d)(?=.*[a-z]))/,
                                    message: '${register_text_17}'//密码至少包含一个英文字母和一个数字
                                }
                            }
                        },
                        conform: {
                            validators: {
                                identical: {
                                    field: 'psw',
                                    message: '${register_text_11}'//两次输入的密码不一致
                                }
                            }
                        }
                    },
                    submitHandler: function (validator, form) {
                        $("#errorMsg").hide(200);
                        var submit = $("[type=submit]");
                        submit.html("<span class='fa fa-circle-o-notch fa-spin fa-2x fa-fw margin-bottom'></span>").attr('disabled', true);
                        //noinspection JSUnresolvedVariable
                        // Use Ajax to submit form data
                        $.post(form.attr('action'), form.serialize(), function (result) {
                            submit.html("${register_text_12}").attr('disabled', false);//注册
                            if (result.state != 1) {
                                //noinspection JSUnresolvedVariable
                                var msg = result.des;
                                $("#errorMsg").html('<span class="fa fa-times-circle" style="margin-right: 10px"></span>' + msg).show(200);
                                if(result.state==8){//用户名密码格式错误
                                    validator.updateElementStatus($("[name=username]"),"INVALID",msg)
                                }
                                if(result.state==9){//该账号已被使用
                                    validator.updateElementStatus($("[name=username]"),"INVALID",msg)
                                }
                                if(result.state==10){//E-mail已被使用
                                    validator.updateElementStatus($("[name=email]"),"INVALID",msg)
                                }
                                return;
                            }
                            bootbox.alert("${register_text_13}", function () {//注册成功
                                window.location = "<%=basePath%>/login.jsp?username=" + $("[name=username]").val() + "&psw=" + $("[name=psw]").val();
                            });
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
