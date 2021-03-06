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
        <br/>【CrazyBomb】网络游戏用户协议
<br/>
        <br/>国家新闻出版总署关于健康游戏的忠告
<br/>
        <br/>《健康游戏忠告》
<br/>
        <br/>抵制不良游戏，拒绝盗版游戏。
<br/>
        <br/>注意自我保护，谨防受骗上当。
<br/>
        <br/>适度游戏益脑，沉迷游戏伤身。
<br/>
        <br/>合理安排时间，享受健康生活。
<br/>
        <br/>重要须知：
<br/>
        <br/>成都市千行科技有限公司（以下又称“千行”或“千行公司”，在《文化部网络游戏服务格式化协议必备条款》当中又被称为“甲方”）在此特别提醒用户（在《文化部网络游戏服务格式化协议必备条款》当中又被称为“乙方”）仔细阅读本《〈CrazyBomb〉网络游戏用户协议》（下称“本《用户协议》”）中的各个条款，包括但不限于免除或者限制千行责任的条款、对用户权利进行限制的条款以及约定争议解决方式、司法管辖的条款。
<br/>
        <br/>请您仔细阅读本《用户协议》（未成年人应当在其法定监护人陪同下阅读），并选择接受或者不接受本《用户协议》。除非您同意并接受本《用户协议》中的所有条款，否则您无权接收、下载、安装、启动、升级、登录、显示、运行、截屏【CrazyBomb】网络游戏，亦无权使用该游戏软件的某项功能或某一部分或者以其他的方式使用该游戏软件。您接收、下载、安装、启动、升级、登录、显示、运行、截屏【CrazyBomb】网络游戏，或者使用该游戏软件的某项功能、某一部分，或者以其他的方式使用该游戏软件的行为，即视为您同意并接受本《用户协议》，愿意接受本《用户协议》所有条款的约束。
<br/>
        <br/>您若与千行因本《用户协议》或其补充协议所涉及的有关事宜发生争议或者纠纷，双方可以友好协商解决；协商不成的，您完全同意双方当中的任何一方均可以将其提交千行所在地广东省深圳市有管辖权的人民法院诉讼解决。
<br/>
        <br/>本《用户协议》分为两大部分，第一部分是文化部根据《网络游戏管理暂行规定》（文化部令第49号）制定的《网络游戏服务格式化协议必备条款》，第二部分是千行根据《中华人民共和国著作权法》、《中华人民共和国合同法》、《著作权行政处罚实施办法》、《网络游戏管理暂行规定》等国家法律法规拟定的【CrazyBomb】网络游戏《用户协议》条款。内容如下：
<br/>
        <br/>第一部分 文化部网络游戏服务格式化协议必备条款
<br/>
        <br/>根据《网络游戏管理暂行规定》（文化部令第49号），文化部制定《网络游戏服务格式化协议必备条款》。甲方为网络游戏运营企业，乙方为网络游戏用户。
<br/>
        <br/>1. 账号注册
<br/>
        <br/>1.1 乙方承诺以其真实身份注册成为甲方的用户，并保证所提供的个人身份资料信息真实、完整、有效，依据法律规定和必备条款约定对所提供的信息承担相应的法律责任。
<br/>
        <br/>1.2 乙方以其真实身份注册成为甲方用户后，需要修改所提供的个人身份资料信息的，甲方应当及时、有效地为其提供该项服务。
<br/>
        <br/>2. 用户账号使用与保管
<br/>
        <br/>2.1 根据必备条款的约定，甲方有权审查乙方注册所提供的身份信息是否真实、有效，并应积极地采取技术与管理等合理措施保障用户账号的安全、有效；乙方有义务妥善保管其账号及密码，并正确、安全地使用其账号及密码。任何一方未尽上述义务导致账号密码遗失、账号被盗等情形而给乙方和他人的民事权利造成损害的，应当承担由此产生的法律责任。
<br/>
        <br/>2.2乙方对登录后所持账号产生的行为依法享有权利和承担责任。
<br/>
        <br/>2.3 乙方发现其账号或密码被他人非法使用或有使用异常的情况的，应及时根据甲方公布的处理方式通知甲方，并有权通知甲方采取措施暂停该账号的登录和使用。
<br/>
        <br/>2.4 甲方根据乙方的通知采取措施暂停乙方账号的登录和使用的，甲方应当要求乙方提供并核实与其注册身份信息相一致的个人有效身份信息。
<br/>
        <br/>2.4.1 甲方核实乙方所提供的个人有效身份信息与所注册的身份信息相一致的，应当及时采取措施暂停乙方账号的登录和使用。
<br/>
        <br/>2.4.2 甲方违反2.4.1款项的约定，未及时采取措施暂停乙方账号的登录和使用，因此而给乙方造成损失的，应当承担其相应的法律责任。
<br/>
        <br/>2.4.3 乙方没有提供其个人有效身份证件或者乙方提供的个人有效身份证件与所注册的身份信息不一致的，甲方有权拒绝乙方上述请求。
<br/>
        <br/>2.5 乙方为了维护其合法权益，向甲方提供与所注册的身份信息相一致的个人有效身份信息时，甲方应当为乙方提供账号注册人证明、原始注册信息等必要的协助和支持，并根据需要向有关行政机关和司法机关提供相关证据信息资料。
<br/>
        <br/>3. 服务的中止与终止
<br/>
        <br/>3.1 乙方有发布违法信息、严重违背社会公德、以及其他违反法律禁止性规定的行为，甲方应当立即终止对乙方提供服务。
<br/>
        <br/>3.2 乙方在接受甲方服务时实施不正当行为的，甲方有权终止对乙方提供服务。该不正当行为的具体情形应当在本协议中有明确约定或属于甲方事先明确告知的应被终止服务的禁止性行为，否则，甲方不得终止对乙方提供服务。
<br/>
        <br/>3.3 乙方提供虚假注册身份信息，或实施违反本协议的行为，甲方有权中止对乙方提供全部或部分服务；甲方采取中止措施应当通知乙方并告知中止期间，中止期间应该是合理的，中止期间届满甲方应当及时恢复对乙方的服务。
<br/>
        <br/>3.4 甲方根据本条约定中止或终止对乙方提供部分或全部服务的，甲方应负举证责任。
<br/>
        <br/>4. 用户信息保护
<br/>
        <br/>4.1 甲方要求乙方提供与其个人身份有关的信息资料时，应当事先以明确而易见的方式向乙方公开其隐私权保护政策和个人信息利用政策，并采取必要措施保护乙方的个人信息资料的安全。
<br/>
        <br/>4.2未经乙方许可甲方不得向任何第三方提供、公开或共享乙方注册资料中的姓名、个人有效身份证件号码、联系方式、家庭住址等个人身份信息，但下列情况除外：
<br/>
        <br/>4.2.1 乙方或乙方监护人授权甲方披露的；
<br/>
        <br/>4.2.2 有关法律要求甲方披露的；
<br/>
        <br/>4.2.3 司法机关或行政机关基于法定程序要求甲方提供的；
<br/>
        <br/>4.2.4 甲方为了维护自己合法权益而向乙方提起诉讼或者仲裁时；
<br/>
        <br/>4.2.5 应乙方监护人的合法要求而提供乙方个人身份信息时。
<br/>
        <br/>第二部分 【CrazyBomb】 网络游戏《用户协议》条款
<br/>
        <br/>5. 名词解释
<br/>
        <br/>本《用户协议》的第二大部分及其补充协议的条款中所用到的下列专有名词，均采用如下解释；除“用户”及“您”这个专有名词外，均使用加粗字体标示：
<br/>
        <br/>5.1 《〈CrazyBomb〉网络游戏用户协议》，即本《用户协议》，简称“【CrazyBomb】用户协议”，指当前的您与千行订立的，关于您在使用和享受千行向您提供的【CrazyBomb】网络游戏产品及服务的过程中，所享有的权利和所负有的义务的软件许可及服务协议。
<br/>
        <br/>5.2 “用户”或“您”，又称“玩家”，即指使用和享受【CrazyBomb】网络游戏产品及服务的自然人，在《文化部网络游戏服务格式化协议必备条款》当中又被称为“乙方”，是本《用户协议》的一方合同当事人。
<br/>
        <br/>5.3 合作单位，指下列五类法人或其他组织的统称，或者其中某一类法人或其他组织中的某一家法人或其他组织，具体所指，依上下文而定：
<br/>
        <br/>（1）第一类：授权千行代理运营【CrazyBomb】，或者授权千行将其享有知识产权的软件或技术运用于【CrazyBomb】当中的法人或其他组织；
<br/>
        <br/>（2）第二类：应千行要求，为千行策划、举办、开展、执行（以下统称“举办”）有关【CrazyBomb】网络游戏的各种地面推广活动（如电子竞技比赛）的法人或其他组织；
<br/>
        <br/>（3）第三类：经千行同意，在【CrazyBomb】网络游戏和/或其官方网站当中投放广告或进行其他的宣传推广活动，或者双方就【CrazyBomb】、合作单位某一种或某几种产品（或服务）品牌联合开展市场推广的法人或其他组织；
<br/>
        <br/>（4）第四类：经千行和/或【CrazyBomb】著作权人、商标注册人授权，通过使用【CrazyBomb】的LOGO、名称、商标或者使用、改编【CrazyBomb】软件要素作品而设计、生产、制（创）作、销售（或发行）【CrazyBomb】游戏衍生品的法人或其他组织；
<br/>
        <br/>（5）第五类：为【CrazyBomb】网络游戏上网运营提供宽带、网络接入、服务器出租、机房出租、信息存储空间、搜索、链接等服务的法人或其他组织；
<br/>
        <br/>（6）第六类：上列四类之外的与千行进行了有关【CrazyBomb】合作事宜的其他的法人或其他组织。
<br/>
        <br/>5.4 千行游戏，是包括【CrazyBomb】在内的千行目前正在运营的所有网络游戏的统称，亦或指千行目前正在运营的某一款或者某几款网络游戏（具体所指，依上、下文而定），包括但不限于：
<br/>
        <br/>（1）千行自主研发并且目前由千行运营的网络游戏；和
<br/>
        <br/>（2）千行代理运营的网络游戏；和
<br/>
        <br/>（3）千行与合作单位联合运营（又叫“合作运营”）的网络游戏。
<br/>
        <br/>5.5 【CrazyBomb】，指当下的这款游戏，亦或指该款游戏所对应的软件以及该软件后续的软件升级包或软件补丁、在线升级等内容。具体所指，依上下文而定。
<br/>
        <br/>【CrazyBomb】游戏软件可以分为封测版、内测版、不删档内测版、公测版、正式运营版、对外测试版等多个版本，均由客户端软件和服务器（即伺服端）软件两个部分构成。
<br/>
        <br/>5.6 软件要素作品，指从游戏软件当中分离出来的可以单独使用的单个作品的统称，是该游戏软件不可分割的组成部分，包括但不限于其中的：
<br/>
        <br/>（1）电子文档、文字、数据库、图片、图表、图饰、图标、照片、程序、音乐、舞蹈、色彩、版面框架、界面设计；
<br/>
        <br/>（2）可以单独构成著作权法意义上的作品的计算机程序、美术图片、文字内容、音乐、歌曲以及舞蹈等内容（又被分别称之为软件要素程序作品、软件要素美术作品、软件要素文字作品、软件要素音乐作品、软件要素歌曲作品和软件要素舞蹈作品）。
<br/>
        <br/>5.7 游戏数据，指您或其他用户在使用和享受【CrazyBomb】网络游戏产品及服务的过程中产生的被服务器软件所实时记录、存储的各种数字、字母、符号和模拟量等的统称，它以计算机语言的形式反映您或其他用户在使用和享受【CrazyBomb】网络游戏产品及服务的过程及结果，包括但不限于记录用户使用和享受【CrazyBomb】网络游戏产品及服务过程的游戏日志以及游戏安全系统检测并记录下来的安全日志。
<br/>
        <br/>5.8 游戏衍生品，指以某一游戏软件为原型，通过直接使用、修改、改编或者其他方式，利用该游戏软件或该游戏软件的软件要素作品、LOGO、名称和/或商标制作出来的物品的统称。从物品存在形态及其价值实现方式的角度，游戏衍生品可分为实物类衍生品和作品类衍生品两种类型；从对游戏软件利用方式及物品形成过程的角度，游戏衍生品可分为游戏过程衍生品、游戏编辑衍生品和游戏改编衍生品三种类型。
<br/>
        <br/>5.8.1 实物类衍生品：是指具有外在的有形实体的衍生品，主要是通过转让所有权、收取购买价款的方式来实现其价值，如玩具、剪纸、衣服等。
<br/>
        <br/>5.8.2 作品类衍生品：是指可以单独构成著作权法意义上的作品的衍生品，主要是通过转让著作权或者著作权许可使用的、收取著作权转让价款或者许可费的方式来实现其价值，如漫画、小说、故事等。
<br/>
        <br/>5.8.3 游戏过程衍生品：即在您或其他用户使用和享受【CrazyBomb】网络游戏产品及服务的过程中，由【CrazyBomb】产生的电子文档、文字、数据库、图片、图表、图饰、图标、照片、程序、音乐、舞蹈、色彩、版面框架、游戏界面等可以单独使用的游戏元素，以及由其形成的截屏、录像、录音等衍生品。
<br/>
        <br/>5.8.4 游戏编辑衍生品：即您或其他用户通过汇编、剪辑、配音、篡改或其他的方式，利用【CrazyBomb】本身设定的地图、场景、人物、游戏规则、故事情节的编辑功能（如有）制作出来的地图和/或游戏规则全部或者部分不同于【CrazyBomb】的新游戏。
<br/>
        <br/>5.8.5 游戏改编衍生品：即您或其他用户以【CrazyBomb】网络游戏及/或其人物角色、游戏道具、游戏场景等元素为原型，通过临摹、模仿、借用、改编或其他的方式，利用【CrazyBomb】之商标、名称、软件、软件要素作品和/或游戏过程衍生品制作出来的非游戏的物品，如玩具、剪纸、折扇、衣服、漫画、小说、电影等。
<br/>
        <br/>5.9游戏大厅，指千行开发的、并单独享有全部著作权及其他知识产权的一款用来为用户提供千行游戏下载、安装、启动、登录、在线使用、链接服务和/或其他相关服务的网络游戏平台。
<br/>
        <br/>5.10 千行游戏论坛，指千行在千行网上开设的、名为“千行游戏社区”的、供用户就千行游戏进行交流的电子公告板。
<br/>
        <br/>5.11 知识产权，指下列任一和全部的知识产权以及其中所有内在的、衍生的和/或相关的权利：
<br/>
        <br/>（1）规程、设计、发明、发现以及由此已经申请到的和正在申请的专利；
<br/>
        <br/>（2）软件、软件要素作品、作品类衍生品、游戏过程衍生品、游戏编辑衍生品及其他作品的著作权、版权以及由其派生的各项权利；
<br/>
        <br/>（3）软件、软件要素作品、作品类衍生品、游戏过程衍生品、游戏编辑衍生品及其他作品的名称权、商标权以及其他形式的公司或产品标识所产生的权利。
<br/>
        <br/>5.12 实名注册，即根据文化部颁布的《网络游戏管理暂行办法》第二十一条规定，千行要求您使用有效的身份证件实名注册自己的个人信息，从而使得您的个人信息与您在【CrazyBomb】网络游戏当中使用的游戏帐号之间建立起一一对应的匹配关系。
<br/>
        <br/>5.13 实名注册系统，又叫“千行游戏帐号实名注册系统”，即根据文化部颁布的《关于贯彻实施网络游戏管理暂行办法的通知》第（八）项所述要求，千行开发建立的供您及其他千行游戏用户进行实名注册的计算机软件系统，网址为：http://www.tsixi.com/tsixi_new/zc.html。
<br/>
        <br/>5.14 实名注册信息，即实名注册系统当中显示的您在其中进行实名注册时首次填写的，以及首次填写之后历次被修改过的您的个人信息的统称，亦可能仅是指实名注册系统当中目前显示的最终的您的个人信息。具体所指，以上下文而定。
<br/>
        <br/>6. 合同目的
<br/>
        <br/>6.1 本《用户协议》的合同目的，旨在为千行许可您使用和享受【CrazyBomb】网络游戏产品及服务提供合同依据，对您基于本《用户协议》在使用和享受【CrazyBomb】网络游戏产品及服务的过程中所享有的权利、所负有的义务进行约定。
<br/>
        <br/>6.2 您在使用和享受【CrazyBomb】网络游戏产品及服务的过程中，可能会使用到第三方授权千行使用的软件或知识产权，该等使用必须是第三方授权范围内的、服从本《用户协议》合同目的的使用。您如果需要将其用于本《用户协议》合同目的之外的用途，请您直接与该等第三方联系，并取得其合法授权。
<br/>
        <br/>7. 知识产权
<br/>
        <br/>7.1 本《用户协议》以及下列任何一项作品或资料的所有权及包括著作权在内的全部知识产权均由千行和/或合作单位享有，受《中华人民共和国著作权法》、《计算机软件保护条例》、《信息网络传播权保护条例》、《中华人民共和国商标法》和相关的国际条约以及其他的法律法规保护：
<br/>
        <br/>（1）【CrazyBomb】之游戏软件；
<br/>
        <br/>（2）【CrazyBomb】之软件要素作品；
<br/>
        <br/>（3）【CrazyBomb】之游戏数据；
<br/>
        <br/>（4）【CrazyBomb】之游戏过程衍生品、游戏编辑衍生品；
<br/>
        <br/>（5）您应千行邀请，为千行提供有关【CrazyBomb】的测试、BUG及外挂跟踪汇报、软文撰写及推广、竞争情报收集等服务的过程中，向千行提交的相应的作品或资料，如游戏测试报告、软文等。
<br/>
        <br/>7.2 应千行邀请，您提供给千行用于【CrazyBomb】的个人作品之著作权归您单独享有，千行享有无期限的、全球范围内的、不可撤销、完全免费的使用权。该等作品一经您提供给千行，即视为您授予了千行该等使用权，而且千行还可以将该等使用权转让或者转授权给其关联公司或者合作单位。双方另有约定的，从其约定。
<br/>
        <br/>7.3 千行基于本《用户协议》许可您的是您对【CrazyBomb】享有非专有使用权。该等非专有使用权，是您对正在使用的【CrazyBomb】当前版本所享有的非专有使用权。而且，该等非专有使用权是临时的、可撤销的、本《用户协议》及其补充协议约定范围内的使用权。
<br/>
        <br/>7.4 本《用户协议》没有也不会将【CrazyBomb】的发行权、信息网络传播权和/或出租权等某一项或某几项著作权权利、及其他的本《用户协议》未明示的权利许可给您，这些权利（或权能）都为千行单独享有。千行通过本《用户协议》许可您的，只是通过互联网在线使用和享受【CrazyBomb】网络游戏产品及服务的权利。
<br/>
        <br/>8. 游戏帐号
<br/>
        <br/>8.1 游戏帐号的所有权归千行，用户完成注册申请手续后，获得游戏帐号的使用权。
<br/>
        <br/>8.2 您如果需要将您享有使用权的游戏帐号作为游戏帐号，使用和享受【CrazyBomb】网络游戏产品及服务，则您需要按照《网络游戏管理暂行规定》及文化部《网络游戏服务格式化协议必备条款》（即本《用户协议》第一部分）的要求，登录实名注册系统并进行实名注册。
<br/>
        <br/>8.3 您能且仅能凭借通过千行提供或者认可的途径、按照千行公布的申请规则申请取得的游戏帐号及设定的密码（又称“游戏密码”），并将其作为游戏帐号使用和享受【CrazyBomb】网络游戏产品及服务。
<br/>
        <br/>8.4 游戏帐号使用权仅属于初始申请注册人，禁止赠与、分配、转让、继受或售卖。如果您并非帐号初始注册人，千行有权在不事先通知您的情况下回收该帐号，由此带来的包括并不限于用户通信中断、个人资料和游戏道具丢失以及无法登录【CrazyBomb】网络游戏等损失由均有您自行承担。
<br/>
        <br/>8.5 千行禁止用户私下有偿或无偿转让游戏帐号，以免因游戏帐号问题产生纠纷，您应当自行承担因违反此要求而遭致的任何损失，同时千行保留追究上述行为人法律责任的权利。
<br/>
        <br/>8.6 您对您的游戏帐号、游戏密码、实名注册以及防沉迷登记的个人信息负有保管责任，并就其帐号及密码项下之一切活动负全部责任。您须重视游戏帐号密码和公开邮箱的密码保护。您保证在您的游戏帐号、密码未经授权而被使用、或者发生其他任何安全问题时，立即通知千行。
<br/>
        <br/>8.7 您充分理解到：为了提高【CrazyBomb】的安全性能，防止您的游戏密码、实名注册以及防沉迷登记的个人信息被他人窃取而导致您无法凭借对应的游戏帐号登录该游戏，千行可能会随时将计算机病毒查杀技术、操作系统修复技术、计算机加密技术等有助于提高【CrazyBomb】网络游戏软件安全性能的计算机硬件或软件运用到【CrazyBomb】当中。即便是如此，并不能免除或者减轻您对游戏帐号及游戏密码等有关资料所负有的本《用户协议》第8.6条所约定的妥善保管义务。对此，您是完全同意的；您如果不同意，请您与千行公司联系。
<br/>
        <br/>8.8 如果您遗忘了游戏密码或者游戏密码被他人修改，将会导致您无法凭借相应的游戏帐号登录【CrazyBomb】，您可以通过千行提供的途径、按照千行公布的申诉规则进行申诉。
<br/>
        <br/>8.9 如果千行游戏帐号实名注册系统显示您的游戏帐号尚未进行实名注册的，请您务必及时进行实名注册，否则您将不能将其作为游戏帐号使用，无法登录和使用包括【CrazyBomb】在内的所有的千行游戏。
<br/>
        <br/>8.10 您充分理解到：千行可能会将您在千行游戏防沉迷登记系统当中登记的您的个人信息纳入到实名注册系统当中，作为您的千行游戏帐号的实名注册信息使用。对此，您是完全同意的；您如果不同意，请您与千行公司联系。
<br/>
        <br/>8.11 千行一向遵守国家有关保护青少年身心健康的法律、政策，按照国家颁布的《网络游戏防沉迷系统开发标准》在【CrazyBomb】当中开发、内置了防沉迷系统。您充分理解到：千行公司可能会将您的实名注册信息运用于防沉迷系统之中，即千行可能会根据您的实名注册信息判断您是否年满18周岁，从而决定是否对您相应的游戏帐号予以防沉迷限制。对此，您是完全同意的；您如果不同意，请您与千行公司联系。
<br/>
        <br/>8.12 用户注册游戏帐号后如果长期不使用，或者有本《用户协议》第9.5条所述行为的，千行公司有权回收帐号，以免造成资源浪费，由此带来的包括并不限于用户通信中断、个人资料、邮件和游戏道具丢失等损失由用户自行承担。
<br/>
        <br/>8.13 本《用户协议》的合同目的，并不是要对您申请游戏帐号、通过申诉找回游戏帐号和/或千行回收QQ 帐号所享有的权利、所负有的义务进行约定。千行对这此另有协议及要求，您如果需要申请游戏帐号、通过申诉找回游戏帐号和/或游戏帐号被回收，应当仔细阅读并充分理解这些协议，并同意接受这些协议及要求的约束。
<br/>
        <br/>9. 用户守则
<br/>
        <br/>9.1 【CrazyBomb】与其他的在线使用的互联网软件一样，您如果要进行下载、安装、启动、登录、显示和/或运行，您至少必须自备一台计算机，在该计算机上安装【CrazyBomb】的客户端软件，并保证其能够通过互联网与【CrazyBomb】的服务器软件进行实时的信息（即电子数据）交互。
<br/>
        <br/>9.2 【CrazyBomb】当中的部分功能和/或游戏，除了需要您具备本《用户协议》第9.1条所述的条件之外，可能还需要您具备其他的一些设备或者软件。例如：【CrazyBomb】当中音响效果需要您具备音响设备。
<br/>
        <br/>9.3 您在使用【CrazyBomb】的收费功能时，应当按照千行的要求支付相应的费用。而且，该等权利属于千行的经营自主权，千行保留随时改变经营模式的权利，即保留变更收费的费率标准、收费的软件功能、收费对象及收费时间等权利。同时，千行和/或合作单位也保留对【CrazyBomb】进行升级、改版，增加、删除、修改、变更其功能或者变更其游戏规则的权利。您如果不接受该等变更的，应当立即停止使用【CrazyBomb】；您继续使用【CrazyBomb】的行为，视为您接受改变后的经营模式。
<br/>
        <br/>9.4 基于本《用户协议》及其补充协议，您可以：
<br/>
        <br/>（1）接收、下载、安装、启动、升级、登录、显示、运行和/或截屏【CrazyBomb】；和/或
<br/>
        <br/>（2）创建【CrazyBomb】游戏角色，设置网名，查阅游戏规则、用户个人资料、游戏对局结果，开设游戏房间、设置游戏参数，使用聊天功能，在游戏中购买、赠送游戏道具、游戏装备、游戏币；和/或
<br/>
        <br/>（3）使用【CrazyBomb】上列功能之外的其他的某一项或某几项功能。
<br/>
        <br/>9.5 您除了可以按照本《用户协议》的约定使用【CrazyBomb】之外，不得进行任何侵犯【CrazyBomb】或其软件要素作品的知识产权的行为，或者进行其他的有损于千行、合作单位或其他用户合法权益的行为。千行及其合作单位也绝对不会允许您从事这些行为，亦有权采取技术措施防止您从事这些行为，包括但不限于：
<br/>
        <br/>（1）删除或修改【CrazyBomb】上的版权信息，或者伪造TCP/IP地址或者数据包的名称；
<br/>
        <br/>（2）进行编译、反编译、反向工程或者以其他方式破解【CrazyBomb】的行为；
<br/>
        <br/>（3）进行任何破坏【CrazyBomb】公平性或者其他影响游戏正常秩序的行为，如主动或被动刷分、合伙作弊、使用游戏外挂或者其他的作弊软件、利用BUG（又叫“漏洞”或者“缺陷”）来获得不正当的非法利益，或者利用互联网或其他方式将游戏外挂、作弊软件、BUG公之于众；
<br/>
        <br/>（4）利用劫持域名服务器等技术非法侵入、破坏【CrazyBomb】之服务器软件系统，或者修改、增加、删除、窃取、截留、替换【CrazyBomb】之客户端和/或服务器软件系统中的数据，或者非法挤占【CrazyBomb】之服务器空间，或者实施其他的使之超负荷运行的行为；
<br/>
        <br/>（5）进行任何诸如发布广告、销售商品的商业行为，或者进行任何非法的侵害千行利益的行为，如贩卖游戏币、外挂、游戏道具、游戏装备等；
<br/>
        <br/>（6）冒充千行、【CrazyBomb】游戏管理员或千行游戏论坛管理员、版主发布任何诈骗或虚假信息；
<br/>
        <br/>（7）发表、转发、传播含有谩骂、诅咒、诋毁、攻击、诽谤千行和/或第三方内容的，或者含有封建迷信、淫秽、色情、下流、恐怖、暴力、凶杀、赌博、反动、扇动民族仇恨、危害祖国统一、颠覆国家政权等让人反感、厌恶的内容的非法言论，或者设置含有上述内容的网名、游戏角色名；
<br/>
        <br/>（8）在【CrazyBomb】当中进行恶意刷屏、恶意踢人、恶意耗时等恶意破坏游戏公共秩序的行为；
<br/>
        <br/>（9）利用【CrazyBomb】故意传播恶意程序或计算机病毒，或者利用【CrazyBomb】发表、转发、传播侵犯第三方知识产权、肖像权、姓名权、名誉权、隐私或其他合法权益的文字、图片、照片、程序、视频、图象和/或动画等资料，发布假冒【CrazyBomb】官方网站网址或链接。
<br/>
        <br/>9.6 未经千行和/或合作单位允许，您不得为下列任何一种行为；您如果要进行下列任何一种行为，请您与千行联系，取得千行的同意，并应千行和/或合作单位的要求与之签订电子的或者纸版的书面合同：
<br/>
        <br/>（1）对【CrazyBomb】进行扫描、探查、测试，以检测、发现、查找其中可能存在的BUG或弱点；
<br/>
        <br/>（2）修改、复制、发行、出租、出版、翻译、汇编、改编和/或转载【CrazyBomb】或其软件要素作品、游戏过程衍生品、游戏编辑衍生品，或者利用互联网或其他的方式将其公之于众；
<br/>
        <br/>（3）建立有关【CrazyBomb】或其软件要素作品、游戏过程衍生品、游戏编辑衍生品的镜像站点，或者进行网页（络）快照，或者利用【CrazyBomb】架设服务器，为他人提供与之完全相同或者类似的互联网服务；
<br/>
        <br/>（4）在【CrazyBomb】当中内置各种插件程序或者其他的第三方程序；
<br/>
        <br/>（5）将软件要素作品从【CrazyBomb】中分离出来单独使用，或者进行其他的不符合本《用户协议》合同目的的使用；
<br/>
        <br/>（6）生产、制作、批发、销售、出版和/或发行游戏改编衍生品；
<br/>
        <br/>（7）使用【CrazyBomb】的名称、商标和/或其软件要素作品；
<br/>
        <br/>（8）参加千行和/或其合作单位举办的有关【CrazyBomb】的电子竞技比赛活动；
<br/>
        <br/>（9）为千行提供有关【CrazyBomb】的测试、BUG及外挂跟踪汇报、软文撰写及推广、竞争情报收集等服务；
<br/>
        <br/>（10）通过互联网或其他方式向千行上传、提供照片、图片、视频、文字等个人作品，以供千行挑选后用于【CrazyBomb】之中；
<br/>
        <br/>（11）实施上列行为之外的需要千行和/或合作单位同意的其他的有关【CrazyBomb】的行为。
<br/>
        <br/>9.7 如果您当前使用的游戏帐号并不是您申请或者通过千行提供的其他途径取得的，但您却知悉了该游戏帐号当前的游戏密码，则请您在第一时间内通知千行或者该游戏帐号的申请人。而且，您不得：
<br/>
        <br/>（1）使用该游戏帐号及游戏密码登录【CrazyBomb】；和/或
<br/>
        <br/>（2）使用该游戏帐号及游戏密码登录、除【CrazyBomb】之外的其他千行游戏、游戏大厅、千行游戏论坛、千行客服官方网站、邮箱和/或享受千行提供的其他的互联网服务；和/或
<br/>
        <br/>（3）修改该游戏帐号项下的游戏密码、申请资料、个人资料；和/或
<br/>
        <br/>（4）利用该游戏帐号买卖、转让、赠与、接受赠游戏币、游戏道具、游戏装备；和/或
<br/>
        <br/>（5）以转让、贩卖、赠与的方式将该游戏帐号或其项下的游戏密码、个人资料游戏币、游戏道具、游戏装备等资料提供给千行和申请人之外的无关的第三方，或者对该游戏帐号或其项下的上述资料进行其他形式的处分；和/或
<br/>
        <br/>（6）通过互联网或者其他的方式将该游戏帐号及其项下的游戏密码、个人资料游戏币、游戏道具、游戏装备等资料公之于众；和/或
<br/>
        <br/>（7）以其他的方式使用或处分该游戏帐号、游戏密码以及该游戏帐号项下的资料。
<br/>
        <br/>9.8 千行在【CrazyBomb】网络游戏官方网站（http://www.tsixi.com/）上为用户提供了【CrazyBomb】客户端软件的下载服务，请您到该网站上下载该客户端软件及其升级版本。
<br/>
        <br/>9.9 千行通过【CrazyBomb】及其官方网站和其他软件，为您提供了诸如“游戏商城”的获得Q币、Q点、游戏币、游戏道具、游戏装备等游戏物品的途径，您可以从千行提供的这些途径获得上述游戏物品。千行不提倡您通过购买、接受赠与或者其他的方式从第三方获得上述游戏物品，尤其是第三方开设的电子网络交易平台网站。因为从第三方获得的上述游戏物品可能是盗号分子盗窃得来的赃物或者存在其他的权利瑕疵，受害者可能会通过各种手段要求索回。
<br/>
        <br/>9.10 千行可能会通过【CrazyBomb】网络游戏官方网站、千行客服官方网站、客服电话、千行游戏论坛、游戏管理员或者其他的途径，向您提供诸如游戏规则说明、BUG或外挂投诉、游戏物品找回、游戏物品锁定或解除锁定、游戏帐号申诉、千行游戏帐号暂时封停、千行游戏帐号实名注册信息修改和/或查验等客户服务，您应当：
<br/>
        <br/>（1）通过千行客服官方网站、千行客服服务电话或者千行提供的其他途径了解这些客户服务的内容、要求以及资费，谨慎选择是否需要享受相应的客户服务，向千行真实地准确地表达您的需求；
<br/>
        <br/>（2）不得在接受千行提供的服务的过程中进行本《用户协议》第9.5条所述的第（7）项行为；
<br/>
        <br/>（3）同意并接受千行关于该等客户服务的专门协议或条款；
<br/>
        <br/>（4）按照千行的要求如实提供您的包括有效身份信息在内的个人信息和游戏情况，及您掌握的其他用户或【CrazyBomb】本身的情况，例如：您的游戏帐户及其项下的个人资料、【CrazyBomb】的登录情况和游戏物品情况，【CrazyBomb】当中存在的BUG、外挂及您知晓的其他玩家使用BUG或外挂的情况。
<br/>
        <br/>9.11 千行在向您提供本《用户协议》第9.10条所述的客户服务的过程中，可能会要求您通过在线填写投诉单，发送电子邮件、截屏、录像，邮寄纸质书信，提供本人有效身份证件或者其他的方式，向千行书面说明您的需求、提供有关情况及证据，您应当如实地、最大限度地、毫无保留地提供。
<br/>
        <br/>9.12 您在享受本《用户协议》第9.10条所述的客户服务的过程中，千行可能不可避免地需要通过互联网对您使用的计算机进行远程协助。您如果请求千行提供该等客户服务，则需要您授予千行进行远程协助的权限，并自行承担由此可能给您造成的损失。
<br/>
        <br/>9.13 千行将会尽最大的努力提高本《用户协议》第9.10条所述的客户服务的质量、满足您的服务要求。即便是如此，千行仍保留向您收取相应的服务费或者其他报酬的权利，而且不保证其提供的服务就一定能够满足您的要求。
<br/>
        <br/>9.14 【CrazyBomb】网络游戏官方网站通过文字、图片或者其他形式，向您介绍【CrazyBomb】的游戏规则。【CrazyBomb】亦是按照这种游戏规则设计、开发的。您是完全同意并承诺按照这种游戏规则进行相应的游戏的；您如果不同意，请您不要下载、安装、启动、登录、显示、运行【CrazyBomb】，您下载、安装、启动、登录、显示和/或运行的行为，即视为您同意并接受这些游戏规则。
<br/>
        <br/>9.15 如果在使用和享受【CrazyBomb】网络游戏产品及服务的过程中，您发现【CrazyBomb】完全或者部分不能实现千行所介绍的对应的游戏规则的，请您立即停止使用不符合游戏规则的这一部分游戏内容或者游戏区域，并在第一时间内通知千行，千行将会尽快进行修复，使之符合这些游戏规则。
<br/>
        <br/>9.16 千行和/或合作单位如果尚未将“CrazyBomb”注册商标的，您不得擅自将其注册商标。否则，您应当配合千行和/或合作单位申请商标局撤销该注册商标，或者将您取得注册商标无偿地、完全地、不可撤销地转让给千行和/或合作单位。
<br/>
        <br/>9.17 您充分理解到：千行和/或合作单位可能会不定期地通过发布软件升级包或软件补丁、在线升级等方式对【CrazyBomb】进行更新。更新的过程中，千行和/或合作单位可能通过互联网调取、收集您的个人计算机上的关于【CrazyBomb】的客户端软件版本信息、数据及其他有关资料，并自动进行替换、修改、删除和/或补充。此种行为是软件更新的所必须的一种操作或步骤，您如果不同意千行和/或合作单位进行此种操作，请您不要进行更新；您更新的行为即视为您同意千行和/或合作单位进行此种操作。
<br/>
        <br/>9.18 您充分理解到：对于【CrazyBomb】来说，本《用户协议》第9.17条所述的某些更新可能是软件版本的更新，如果您不进行此种更新则将无法登录【CrazyBomb】。而且，此种更新将会导致您使用的计算机上原有的软件版本完全被新的软件版本替换掉。
<br/>
        <br/>9.19 您充分理解到：游戏数据将会占据【CrazyBomb】的服务器空间。长时间保留您在使用和享受【CrazyBomb】网络游戏产品及服务的过程中所产生的全部游戏数据，将会大量地挤占服务器空间，影响您及其他【CrazyBomb】用户的游戏速度，增加千行的运营成本，是完全没有必要的。因此，千行和/或合作单位将会定期将服务器上存储的一些过往的游戏数据转移或者永久地删除。对此，您是完全同意的；您如果不同意，请您与千行公司联系。
<br/>
        <br/>9.20 为了测试【CrazyBomb】的功能、用户承载能力、查找其中可能存在的BUG或者进行其他的检测其品质的行为，千行将会在【CrazyBomb】对外正式发布（又称“公测”）之前或之后发布一些供用户体验、测试并反馈意见的软件测试版本，并通过向您提供激活码、该版本客户端软件下载的网络链接地址、发送客户端软件等形式邀请您参加体验、测试。而且，千行可能会向用户同时提供两种或两种以上版本的【CrazyBomb】网络游戏产品，而其中的某些版本仅限于由某一部分用户登录使用，其他的用户则不能登录使用。
<br/>
        <br/>9.21 您充分理解到：本《用户协议》第9.20条所述的软件测试版本，并不是向所有的用户公开的，请您不要把您知晓的激活码、客户端软件下载的网络链接地址告诉他人，也不要将客户端软件提供给他人。而且，您应当按照千行的要求如实地、毫无保留地、准确地、完全地将您在体验、测试过程中发现的诸如存在BUG情况告诉千行。而且，未经千行同意，您不得将该等情况提供给第三方，或者通过互联网或其他方式将其公之于众。
<br/>
        <br/>9.22 您充分理解到：本《用户协议》第9.20条所述的软件测试版本，只是千行和/或合作单位用来供部分用户体验、测试的临时的版本，千行和/或合作单位将会在认为其已经完成使命的时候将该版本之服务器软件从服务器上删除，或者用新的软件版本将其替换掉。一经删除或者替换：
<br/>
        <br/>（1）您将不能继续登录、使用该版本；
<br/>
        <br/>（2）您在体验、测试过程中产生的游戏数据将会被永久性删除；和/或
<br/>
        <br/>（3）您在体验、测试过程中取得的游戏道具、游戏装备、积分、等级或者荣誉等将会被永久性删除，您将永远不能在【CrazyBomb】使用这些资料，即便是您使用同一个游戏帐号登录、使用该软件的其他的版本。
<br/>
        <br/>上述类型的软件测试版本，一般都是正式运营版本以及不删档内测版本和公测版本之外的其他的软件测试版本，包括但不限于封测版本、内测版本和外部测试版本。
<br/>
        <br/>9.23 您充分理解到：您在使用和享受【CrazyBomb】网络游戏产品及服务（包括但不限于享受本《用户协议》上述第9.10条所述的客户服务）的过程中，千行和/或合作单位可能会借助cookie收集您使用【CrazyBomb】的情况。对此，您是充分理解并完全同意的。您如果不同意，可以通过设置您使用的计算机上的Internet选项来进行规避。
<br/>
        <br/>9.24 您承诺并保证：您对您通过本《用户协议》第12.3条第（2）、（3）、（4）、（7）、（8）款所述的方式提供给千行的作品享有完全的著作权，或者虽然不享有著作权，但享有将其许可给千行和/或合作单位用于【CrazyBomb】之中的权利。而且，该等使用既可能是将其用于【CrazyBomb】开发之中，也可能是将其用于【CrazyBomb】的市场推广活动（如广告）之中。
<br/>
        <br/>9.25 千行和/或合作单位可能会在使用您提供的作品之前，要求您出具书面的授权证明及您的身份证明（如身份证复印件）。如果千行和/或合作单位在使用您提供的作品的同时，需要一并使用您的肖像、姓名或者其他合法权益的，您同意将您的肖像、姓名和/或其他合法权益一并授权给千行和/或合作单位使用。
<br/>
        <br/>9.26 您在参加千行或其合作单位举办包括电子竞技比赛活动在内的地面推广活动时，应当服从千行或其合作单位的安排，遵守相应的活动规则。如果您是未成年人，请您不要参加该等地面推广活动；如果您坚持要参加，请您征求您的法定监护人的意见，取得他们的同意，并承担由此可能造成的一切后果。
<br/>
        <br/>10. 限制使用
<br/>
        <br/>10.1 您如有下列行为之一的，千行有权采取本《用户协议》第10.2条所述措施中的一种或几种。给千行造成经济损失的或者有损千行商业信誉、企业形象的，千行可以要求您另行给予赔偿并公开赔礼道歉；构成国务院《著作权行政处罚实施办法》或者其他法律法规所规定的违法行为的，千行将提请国家版权局等有关行政管理机关对您进行行政处罚；构成犯罪的，千行将向有关的司法机关举报，追究您的刑事责任：
<br/>
        <br/>（1）违反本《用户协议》第9.3条约定，拒不付费或者延迟支付相关费用的；
<br/>
        <br/>（2）违反本《用户协议》第9.5条约定，从事该条所列行为之一的；
<br/>
        <br/>（3）违反本《用户协议》第9.6条约定，未经千行允许，从事该条所列行为之一的；
<br/>
        <br/>（4）违反本《用户协议》第9.7条约定，在知悉他人的游戏帐号及游戏密码后进行侵犯他人使用权的行为。
<br/>
        <br/>（5）违反本《用户协议》第9.21条约定，或在向千行提供第9.6条第（9）项所述服务的过程中，将获得的软件测试版本的客户端软件提供给他人，或者将体验、测试过程中获知的诸如存在BUG的情况提供给第三方的。
<br/>
        <br/>10.2 您如有本《用户协议》第10.1条所列行为之一的，千行有权采取下列措施当中的一种或几种：
<br/>
        <br/>（1）踢人，即立即断开您当前使用的计算机与【CrazyBomb】服务器之间的网络连接，您必须重新登录才能继续使用【CrazyBomb】；
<br/>
        <br/>（2）暂时封号，即暂时封停您的游戏帐号，使之在封停期限内不能登录【CrazyBomb】或其某一游戏区域；
<br/>
        <br/>（3）封停游戏角色，即暂时封停您当前使用的游戏帐号项目下的某一个或者几个【CrazyBomb】游戏角色，使之在封停期限内不能使用；
<br/>
        <br/>（4）监禁，即暂时将您当前游戏帐号项下的某一【CrazyBomb】游戏角色的活动范围限定在某一特定的游戏区域，使之在监禁期限内不能离开这一特定的游戏区域；
<br/>
        <br/>（5）暂停欠费功能，即暂时禁止您使用【CrazyBomb】当中某一要求付费的功能，直至您清偿所欠费用并为继续使用上述付费功能而预先支付相应的费用之日止；
<br/>
        <br/>（6）降级，即降低或者清除您当前使用的游戏帐号在【CrazyBomb】当中的积分、等级和/或荣誉；
<br/>
        <br/>（7）暂时禁言，即暂时禁止您凭借当前使用的游戏帐号在【CrazyBomb】当中发表任何言论，使之在禁言期限不能发表任何言论；
<br/>
        <br/>（8）删除广告、虚假信息和/或非法言论，即永久性地、不可撤销地将您发布的广告、虚假信息或者非法言论删除，或者采取其他的阻止其传播的措施；
<br/>
        <br/>（9）删除赃物，即永久性地、不可撤销地将您非法获取的游戏币、游戏道具和/或游戏装备等游戏物品删除，或者将其返还给原来的通过合法途径取得其使用权的其他用户；
<br/>
        <br/>（10）清零，即永久性地、不可撤销地将您非法获取的积分、等级和/或荣誉取消或清零；
<br/>
        <br/>（11）永久禁言，即永久性地、不可撤销地禁止您凭借当前使用的游戏帐号在【CrazyBomb】当中发表任何言论；
<br/>
        <br/>（12）拒绝受理帐号申诉，即拒绝向您提供本《用户协议》第9.10条所述的客户服务和/或第8.8条所述的游戏帐号申诉服务，即便是该等服务是您所需要的，并为您所请求提供的；
<br/>
        <br/>（13）禁止申请新的游戏帐号或者游戏帐号，即禁止您通过申请或者其他任何方式取得新的游戏帐号，或者即便是可以申请取得游戏帐号，但申请取得的游戏帐号不能用作游戏帐号；
<br/>
        <br/>（14）清空数据，即永久性地、不可撤销地删除您当前使用的游戏帐号项下的某一游戏角色及依附于该游戏角色的所有游戏道具、游戏装备、游戏币、积分、等级、荣誉等资料以及相应的游戏数据；
<br/>
        <br/>（15）永久封号，即永久性地、不可撤销地禁止您凭借在当前使用的游戏帐号登录【CrazyBomb】，并删除、清空该游戏帐号在【CrazyBomb】当中产生的所有游戏数据、游戏币、游戏道具、游戏装备等资料，但您仍可以凭借该游戏帐号正常消费千行提供的其他的互联网服务；
<br/>
        <br/>（16）回收帐号，即永久性地、不可撤销地禁止您凭借当前使用的游戏帐号使用千行提供的包括【CrazyBomb】在内的所有互联网服务，并将该游戏帐号回收；
<br/>
        <br/>（17）其他措施，即采取上列措施之外的其他的措施。
<br/>
        <br/>10.3 针对您实施的本《用户协议》第10.1条所述的行为，千行可能会连续地、间断地或者交替地采取本《用户协议》第10.2条所述的措施当中的一种或几种。
<br/>
        <br/>例如：在您违反本《用户协议》第9.5条约定而进行了该条所述的第（3）项行为时，千行可能会对您当前使用的游戏帐号采取本《用户协议》第10.2条第（1）项所述的踢人措施，立即断开您当前使用的计算机与【CrazyBomb】服务器之间的网络连接，您必须重新登录才能继续使用【CrazyBomb】。
<br/>
        <br/>如您再次进行了本《用户协议》第9.5条所述的第（3）项行为的，千行可能会对您当前使用的游戏帐号采取本《用户协议》第10.2条第（2）项所述的暂时封号措施，暂时禁止您凭借当前使用的游戏帐号登录【CrazyBomb】；也可能会采取第10.2条所述的第（9）、（10）或（15）项措施。
<br/>
        <br/>10.4 千行可能会通过在【CrazyBomb】登录窗口及其官方网站或者其他的途径公布该游戏存在的BUG，一经公布，您应当立即停止使用BUG的行为。否则，千行可能会采取本《用户协议》第10.2条所述的第（2）、（3）、（15）项或第（16）项处理措施。
<br/>
        <br/>即便是如此，亦不能免除在千行公布之前您负有的本《用户协议》第9.5条所述的第（3）项义务。换言之，即便是没有对外公布BUG，千行也可以自行判断您是否使用了BUG。而且，千行一经判断您使用了BUG，即可对您当前使用的游戏帐号采取上述措施。
<br/>
        <br/>10.5 您提请千行提供本《用户协议》第9.10条所述的客户服务时，如不按照第9.11条之约定提供相应的资料及证据的，千行有权拒绝提供相应的客户服务。
<br/>
        <br/>10.6 如果您违反本《用户协议》第9.24条或者第9.25条之约定，有下列行为之一的，千行可以拒绝采用您提供的作品；如给千行造成经济损失，千行可以要求您给予相应的赔偿，包括但不限于千行因此被第三人索赔、起诉要求支付的赔偿金：
<br/>
        <br/>（1）拒绝提供书面的授权证明、个人身份证明的；
<br/>
        <br/>（2）将您不享有著作权的作品许可给千行使用的；
<br/>
        <br/>（3）未经著作权人或者合法权利人同意，擅自将第三人作品许可给千行使用的；
<br/>
        <br/>（4）千行使用您提供的作品时，需要同时使用您的肖像、姓名或者其他合法权益而您拒绝将其授权千行使用的。
<br/>
        <br/>10.7 您如果违反本《用户协议》第9.26条之约定，在参加千行或其合作单位举办的地面推广活动时，您是未成年人，或者虽然不是未成年人，但不服从千行或其合作单位的安排，或不遵守相应的活动规则的，千行或其合作单位有权取消您的参与资格。
<br/>
        <br/>11. 免责声明
<br/>
        <br/>11.1 如果您不具备本《用户协议》第9.1所述的条件，您将不能使用【CrazyBomb】；如果您不具备本《用户协议》第9.2所述的条件，您将不能使用【CrazyBomb】的部分功能。千行对此无须向您承担任何责任。
<br/>
        <br/>11.2 【CrazyBomb】与其他的在线使用的互联网软件一样，也会受到各种不良信息、网络安全和网络故障问题的困扰，包括但不限于：
<br/>
        <br/>（1）其他用户可能会发布诈骗或虚假信息，或者发表含有谩骂、诅咒、诋毁、攻击内容的，或含有淫秽、色情、下流、反动、煽动民族仇恨等让人反感、厌恶的内容的非法言论；
<br/>
        <br/>（2）其他用户可能会发布一些侵犯您或者其他第三方知识产权、肖像权、姓名权、名誉权、隐私和/或其他合法权益的图片、照片、文字等资料；
<br/>
        <br/>（3）面临着诸如黑客攻击、计算机病毒困扰、系统崩溃、网络掉线、网速缓慢、程序漏洞等问题的困扰和威胁。
<br/>
        <br/>11.3 您充分理解到：上述第11.2条所述的各种不良信息、网络安全和网络故障问题，并不是千行、合作单位或者【CrazyBomb】所导致的问题，由此可能会造成您感到反感、恶心、呕吐等精神损害，或者给您造成其他的损失，一概由您自行承担，千行和/或合作单位无须向您承担任何责任。
<br/>
        <br/>11.4 您充分理解到：从第三方获得游戏币、游戏道具、游戏装备等游戏物品可能存在各种风险。您不得从第三方获得上述游戏物品，您如果坚持从第三方获得上述游戏物品，则您应当自行承担相应的风险，千行不保证该等游戏物品在【CrazyBomb】能够正常使用，也不保证该等物品不被索回。
<br/>
        <br/>11.5 您充分理解到：用户从事千行所禁止的本《用户协议》第9.5条所约定的第（3）、（4）项和/或第（9）项行为，是一种即时性的瞬间消失的行为。目前，网络游戏企业通常是在服务器软件和/或客户端软件中设置安全程序，由这种安全程序对接收到的从用户使用的计算机上传输过来的游戏数据进行分析判断。如果接收到游戏数据符合这种安全程序当中事先设定的多项分析指标，或者多次符合其中设定的某一项或某几项指标，则网络游戏企业可能据此判断用户从事了本《用户协议》第9.5条所约定的第（3）、（4）项和/或第（9）项行为。当然，网络游戏企业也可能会采取与上列方式不同的方式进行分析判断。
<br/>
        <br/>11.6 您充分理解到：网络游戏企业根据安全程序所做出的分析判断都不是100%准确无误的，千行也不例外。千行将尽最大的努力提高本《用户协议》第11.5条所述的安全程序的性能。但千行不保证也不承诺：其通过该种安全程序所作出的分析、判断就一定是100%准确的。对此，您是给予充分理解并谅解的。如果千行做出了错误判断据此判断给您造成了损失的，您愿意自行承担，千行将不给予任何赔偿。
<br/>
        <br/>11.7 您充分理解到：用户从事千行所禁止的本《用户协议》第9.5条所约定的第（5）、（6）、（7）、（8）项和/或第（9）项行为，是一种即时性的瞬间即可让众多用户知晓的行为。千行如果不立即采取诸如本《用户协议》第10.2条所述的第（7）、（8）或（11）项的处理措施，可能会造成非常严重非常恶劣的后果。对此，您是给予充分理解的，并完全同意千行采取上述措施对上述行为进行处理。
<br/>
        <br/>11.8 您如果对千行就您使用的游戏帐号采取的本《用户协议》第10.2条所述的措施有异议，则应当在知道千行采取该等措施之日7个工作日内向千行客户服务部反馈，提供相应的情况，说明您的异议理由。千行将会根据您提供的情况及说明的理由自行判断是否应当终止执行该等措施。但这不会导致该等措施无效，也不影响异议期间该等措施的执行。千行客户服务部如果在采取该等措施3个月未接到任何反馈的，则视为您没有任何异议，千行不再接受任何有关的反馈。
<br/>
        <br/>11.9 您充分理解到：千行采取本《用户协议》第10.2条所述的第（2）项、第（3）项和/或第（5）项措施时，可能会导致您当前使用的游戏帐号项下的有使用期限的游戏道具、游戏装备因使用期限在千行采取上述措施期间届满而无法继续使用，由此给您造成损失由您自行承担，千行将不给予任何赔偿。换言之，千行采取上述措施所持续的时间是计算在有使用期限的游戏道具、游戏装备的使用期限当中的；该等措施执行终止后，游戏道具、游戏装备的使用期限并不会因此续延。
<br/>
        <br/>11.10 【CrazyBomb】网络游戏和/或其附属的商城内出售的游戏物品，没有标明使用期限，或者其标明的使用期限为“永久”、“无限期”或“无限制”的游戏物品，仅指在千行运营【CrazyBomb】网络游戏期间可以无限期使用，其使用期限即为自您获得该游戏物品之日起至【CrazyBomb】网络游戏终止运营之日止。一旦因为各种原因导致本《用户协议》被终止或者【CrazyBomb】网络游戏终止运营，您将无法继续使用该等游戏物品，千行和/或合作单位对您本人或任何第三方均不负任何损害赔偿责任。
<br/>
        <br/>11.11 您充分理解到：正如本《用户协议》第9.8条所述，千行已经为您提供了下载【CrazyBomb】网络游戏客户端软件的途径。您如果从千行提供的途径之外的途径获得【CrazyBomb】网络游戏客户端软件的，千行不保证其可用性，也不保证其中一定不含有计算机病毒或者其他的恶意程序，由此给您造成的任何损失均由您自行承担，千行将不给予任何赔偿。
<br/>
        <br/>11.12 您充分理解到：正如本《用户协议》第9.10条所述，千行已经为您提供了享受该条所述的客户服务的途径。您如果从千行提供的途径之外的途径享受该种服务，则该等服务绝对不会是千行提供的，千行请您仔细甄别并进行区分。您如果从千行提供的途径之外的途径享受该等服务，由此给您造成的任何损失均由您自行承担，千行将不给予任何赔偿。
<br/>
        <br/>11.13 您充分理解到：千行通过本《用户协议》第9.14条所述的方式向您介绍的游戏规则可能是不充分的、有限的。千行不保证对【CrazyBomb】游戏规则的介绍是完全的、充分的、没有任何错漏的，也不保证【CrazyBomb】能够完全地、充分地、没有任何错漏地实现这些游戏规则。
<br/>
        <br/>11.14 您充分理解到：千行为了有效遏制【CrazyBomb】中用户使用BUG或外挂的行为，可能会采取停机维护或者回档的处理措施。停机维护期间，您可能会不能登录【CrazyBomb】网络游戏的某些游戏服务器和/或某些游戏区域。回档，则是将所有用户游戏帐号项下的游戏数据从某一个时刻的记录返回到此前的另一个时刻的记录，由此可能会导致您在这期间通过正常游戏行为获得的游戏币、游戏道具、游戏装备等游戏物品和等级、积分、荣誉等永久性丧失，或者给您造成其他的损失。对此，您充分理解并愿意自行承担，千行将不给予任何赔偿。
<br/>
        <br/>11.15 您充分理解到：合作单位在【CrazyBomb】网络游戏中投放的广告或者进行其他的宣传推广之内容（以下统称“合作单位广告内容”），均是由合作单位自行提供的，【CrazyBomb】提供的可能仅仅是链接或者内置服务。您应当通过直接与这些合作单位联系等方式自行判断其真实性，千行对合作单位广告内容不作任何明示或者默示的担保。
<br/>
        <br/>11.16 您充分理解到：合作单位广告内容所对应的商品或服务系由合作单位单独生产或经营的。千行没有也不会以分成或者其他任何方式从合作单位销售的该等商品或服务当中取得任何收入，千行没有也不会对该等商品或服务之质量作任何明示或者暗示的担保。您如果需要购买该等商品或者消费该等服务，请直接与合作单位联系，自行评估商品或者服务的质量及对价，并自行决定是否要购买或者消费，一概与千行无关。
<br/>
        <br/>11.17 您充分理解到：千行可能会将合作单位提供的某些服务（如合作单位提供的某些软件的有期限限制的使用权或旅游服务）或者实物作为奖品赠送给您，千行将会按照您提供的联系方式与您联系，并将这些服务或实物提供给您。但千行对该等服务和/或服务的质量不作任何明示或者默示的担保，如该等服务和/或实物存在质量问题，请直接与相应的合作单位联系，并自行与之解决相关的争议或纠纷，一概与千行无关。
<br/>
        <br/>11.18 您充分理解到：千行根据本《用户协议》第8.12条或者第10.2条第（16）款所述约定对您的游戏帐号进行回收，将会给您造成一定的损失，您完全同意该等损失均由您自行承担，千行不必给予任何赔偿。该等损失包括但限于本《用户协议》第8.12条所述的损失。
<br/>
        <br/>11.19 您充分理解到：【CrazyBomb】可能会因千行本身或者合作单位的互联网系统软硬件设备的故障、失灵、或人为操作的疏失而全部或一部分中断、暂时无法使用、迟延，或因他人侵入【CrazyBomb】系统篡改或伪造变造资料等，造成【CrazyBomb】的停止或中断者或用户档案缺失，您不得要求千行给予您任何的补偿或赔偿。
<br/>
        <br/>11.20 您同意您使用和享受【CrazyBomb】网络游戏产品及服务是出于您个人意愿，并愿自行承担任何风险及由此给您造成的任何直接、间接、衍生的损害，千行和/或合作单位不承担任何责任。若依法无法完全排除损害赔偿责任时，千行和合作单位的赔偿责任亦以返还用户因此支付千行的价款为限。
<br/>
        <br/>11.21 您充分理解到：【CrazyBomb】网络游戏当中可能会设置一些强制要求您与其他用户或者【CrazyBomb】网络游戏自设的游戏角色进行对战的游戏区域（例如某一服务器或者某一个服务器当中的某一特定游戏区域，亦或是所有的服务器），您如果不同意强制对战，请您立即离开这些游戏区域。如果您没有离开这些游戏区域，即视为您同意强制对战，并接受强制对战在游戏中产生的后果。
<br/>
        <br/>11.22 千行保留随时地、不事先通知地、不需要任何理由地、单方面地中止履行本《用户协议》及终止本《用户协议》的权利。该等中止、终止，可能是因为千行公司解散、注销、合并、分立，也可能是因为合作单位将【CrazyBomb】或其运营权转让给了第三方，还可能是因为国家法律、法规、政策及国家机关的命令或者其他的诸如地震、火灾、海啸、台风、罢工、战争等不可抗力事件，还可能是上列原因之外的其他原因。
<br/>
        <br/>11.23 本《用户协议》如因为本《用户协议》第11.22条所述原因：
<br/>
        <br/>（1）被中止，则包括您在内的所有用户将暂时不能凭借游戏帐号登录【CrazyBomb】，直至本《用户协议》中止期限届满之日止；
<br/>
        <br/>（2）被终止，则包括您在内的所有用户将永远不能凭借游戏帐号登录【CrazyBomb】，千行将以公告或者补充协议的形式处理相关事宜。
<br/>
        <br/>12. 用户信息保护
<br/>
        <br/>12.1您充分理解到：保护用户信息是千行一贯的政策。对于您和其他【CrazyBomb】用户的个人信息，千行将按照《网络游戏管理暂行规定》、本《用户协议》以及实名注册系统当中公布的《千行游戏用户个人信息及隐私保护政策》对其进行保护。千行绝对不会将其披露给无关的第三方，更不会将其公之于众，但因下列原因而披露给第三方的除外：
<br/>
        <br/>（1）基于国家法律法规的规定而对外披露；
<br/>
        <br/>（2）应国家司法机关及有法律赋予权限的政府机关基于法定程序的要求而披露；
<br/>
        <br/>（3）为保护千行、合作单位或您的合法权益而披露；
<br/>
        <br/>（4）在紧急情况下，为保护其他用户及第三人人身安全而披露；
<br/>
        <br/>（5）经用户本人（或其监护人）同意或应用户（或其监护人）的要求而披露。
<br/>
        <br/>12.2您充分理解到：与其他的互联网企业及互联网软件产品一样，您通过下列途径产生或者主动提供给千行的个人信息、数据、作品或其他资料，均有可能被千行所保存或销毁，千行亦有可能对其进行整理、统计和分析，从而最终形成并获得一种仅仅揭示和反映【CrazyBomb】产品使用情况而不显示您的个人信息的统计数据（例如【CrazyBomb】注册用户数、同时在线用户数、付费用户比例等），以供千行和/或合作单位用于了解【CrazyBomb】的使用情况、持续改进【CrazyBomb】的功能、提升【CrazyBomb】产品的品质：
<br/>
        <br/>（1）您在进行实名注册时，在实名注册系统当中填写的实名注册信息；
<br/>
        <br/>（2）您在申请、申诉游戏帐号、享受本《用户协议》第9.10条所述的客户服务的过程中填写、提供给千行的个人信息、作品或其他资料；
<br/>
        <br/>（3）您为千行提供有关【CrazyBomb】的测试、BUG及外挂跟踪汇报、软文撰写及推广、竞争情报收集等服务时提供给千行的个人信息、数据、作品、游戏过程衍生品或其他资料；
<br/>
        <br/>（4）您通过互联网或其他方式向千行上传、提供照片、图片、视频、文字等个人作品或者游戏过程衍生品；
<br/>
        <br/>（5）您在使用和享受【CrazyBomb】网络游戏产品及服务的过程中产生的游戏数据；
<br/>
        <br/>（6）千行和/或合作单位通过本《用户协议》第9.12、9.17或9.23条所述的途径收集到的您使用的计算机上的数据或个人信息；
<br/>
        <br/>（7）您在参加【CrazyBomb】网络游戏地面推广活动时提供给千行和/或合作单位的个人信息、数据、作品、游戏过程衍生品或其他资料；
<br/>
        <br/>（8）您以其他方式向千行提供的，或者让千行知晓的个人信息、数据、作品、游戏过程衍生品或其他资料。
<br/>
        <br/>13. 其他约定
<br/>
        <br/>13.1 千行保留随时地、不事先通知地、不需要任何理由地、单方面地修订本《用户协议》的权利。本《用户协议》一经修订，千行将会用修订后的协议版本完全替代修订前的协议版本，并通过原有方式向所有用户公布。您应当及时关注和了解本《用户协议》的修订情况，如果您不同意修订后协议版本，请您立即停止使用和享受【CrazyBomb】网络游戏产品及服务，否则即视同您同意并完全接受修订后的协议版本。
<br/>
        <br/>13.2 千行将本《用户协议》以对话框的形式内置于【CrazyBomb】客户端软件安装程序当中，您在安装该客户端软件的过程中即可查阅、了解本《用户协议》，亦可以通过点击“我同意”或“我接受”表示您完全同意并接受本《用户协议》之约束，或者点击“我不同意”或“我不接受”表示您不同意本《用户协议》。
<br/>
        <br/>13.3 除了本《用户协议》第13.2条所述的方式之外，您还可以通过登录、浏览【CrazyBomb】官方网站来 查阅、了解本《用户协议》。千行以“已阅读并同意《用户协议》”按钮的形式，在【CrazyBomb】登录窗口为您提供了登录【CrazyBomb】官方网站的链接地址，以便您直接查阅本《用户协议》当前版本。您每次登录【CrazyBomb】时可以通过点击该按钮随时查阅、了解本《用户协议》。如果您不同意本《用户协议》，请您取消该按钮前面方框内的“●”或“√”，否则视同您完全同意并接受本《用户协议》之约束。
<br/>
        <br/>13.4 千行将会不定期地以“玩家条例”、“用户守则”、“封号规则”、公告等补充协议的形式对本《用户协议》进行补充、修订。该等补充协议将会在【CrazyBomb】官方网站上对外公布，是本《用户协议》不可分割的组成部分，与之具有同等的法律效力。您应当实时查阅并了解这些补充协议。该等补充协议与本《用户协议》有矛盾或者冲突的地方，适用该等补充协议；没有涉及的内容，仍适用本《用户协议》的有关约定。
<br/>
        <br/>13.5 【CrazyBomb】如采用了除千行之外的第三方知识产权，而该等第三方对您基于本《用户协议》在【CrazyBomb】当中使用该等知识产权有要求的，则第三方的该等要求将会以本《用户协议》第13.4条所述的补充协议的形式向您公布，您亦应当一并遵守。
<br/>
        <br/>13.6 千行为贯彻执行文化部颁布的《关于贯彻实施网络游戏管理暂行办法的通知》的第（八）项规定，已经制定了《千行游戏用户个人信息及隐私保护政策》，并且已经在实名注册系统进行了公布，网址为：http://www.tsixi.com/，内容请见本《用户协议》附件。
<br/>
        <br/>    13.7 《千行游戏用户个人信息及隐私保护政策》是本《用户协议》的补充协议，与本《用户协议》有矛盾或者冲突的地方，适用《千行游戏用户个人信息及隐私保护政策》；没有涉及的内容，仍适用本《用户协议》的有关约定。
<br/>
        <br/>    13.8 本《用户协议》各条款是可分的，所约定的任何条款如果部分或者全部无效，不影响该条款其他部分及本《用户协议》其他条款的法律效力。
<br/>
        <br/>    13.9 本《用户协议》各条款的标题只是为了方便用户阅读而起到提示、醒目的作用，对本《用户协议》的解释及适用没有任何指引作用。
<br/>
        <br/>    13.10 千行基于本《用户协议》及其补充协议的有效的弃权必须是书面的，并且该弃权不能产生连带的相同或者类似的弃权。
<br/>
        <br/>    13.11 【CrazyBomb】官方网站为：http://www.tsixi.com/，您如对本《用户协议》条款有任何疑问或者需要帮助，可以通过上述网站与千行联系。
<br/>
        <br/>    13.12 本《用户协议》及其补充协议签订地为四川省成都市高新区，均受中华人民共和国法律、法规管辖，千行保留最终解释权。
<br/>
        <br/>    成都市千行科技有限公司
<br/>
        <br/>    2016年5月


    </div>
    <div class="row">
        <a class="btn btn-info btn-block" href="<%=basePath%>/login.jsp">${identify_text_18}</a>
    </div>
</div>
</body>
</html>
