<%--
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
    request.setAttribute("host", request.getServerName());
%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>TSDK</title>
    <script src="<%=basePath%>/resources/js/jquery.min.js" type="text/javascript"></script>
    <script>
        (function ($) {
            $.getUrlParam = function (name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
                var r = window.location.search.substr(1).match(reg);
                if (r != null) return unescape(r[2]);
                return null;
            }
        })(jQuery);
        <c:if test="${host=='localhost'}">
        var tsixiLoginInterface = {
            getLoginInfo: function () {
                return "{\"channel\":\"2\",\"appID\":\"2\",\"deviceName\":\"test\",\"language\":\"zh_CN\"}";
            }
        };
        </c:if>
        var onReady = function () {
            var userInfo = $.parseJSON(tsixiLoginInterface.getLoginInfo());
            if (userInfo.language) {
                window.location = "<%=basePath%>/index.jsp?language=" + userInfo.language;
            }
        };
        <c:if test="${host=='localhost'}">
        $(document).ready(function () {
            onReady();
        });
        </c:if>
    </script>
</head>
<body>
</body>
</html>
