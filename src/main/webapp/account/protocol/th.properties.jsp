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
        <h1>Games User Agreement</h1>
        <br>IMPORTANT! PLEASE READ CAREFULLY.
        <br>THIS SOFTWARE IS LICENSED, NOT SOLD. BY INSTALLING, COPYING OR OTHERWISE USING THE GAME (DEFINED BELOW), YOU AGREE TO BE BOUND BY THE TERMS OF THIS AGREEMENT.
        <br>If you are under 18 age, please read this agreement accompanied by legal guardian, and pay special attention to minor's terms of use.
        <br>1、 Definition
        <br>1.1 This agreement: refers to the body of this agreement, "The Tsixi of service agreement, the rules of the game and its revision. The above content is officially released, is an integral part of this agreement. This agreement also includes culture according to the network game management interim measures "(culture ministry make no. 49) of the network game service format the essential clauses in the agreement.
        <br>1.2 The rules of the game: refers to the Tsixi game service providers and revised from time to time about the Tsixi of game user codes, regulations, game announcement and notice.
        <br>Line 1300 game service providers: point to your provide game the Tsixi of chengdu technology co., LTD., referred to in this agreement as the "one thousand".
        <br>The Tsixi of the game: refers to the the Tsixi shall be responsible for the operation of the game collectively, including computer client games, web games, mobile games in the form of the game; the Tsixi games might offer in the form of software, in this case, the Tsixi include the related software and related documentation.
        <br>The Tsixi game service: refers to the Tsixi provide you with online operation services related to the game.
        <br>1.6 You also called "players" or "user", refers to any natural person having been authorized the use of the Tsixi of the game and its service.
        <br>1.7 Game data: refers to the line you are using the Tsixi games of various data was recorded by the com.u8.server, including game logs, such as security log data.
        <br>2、Changes and Effective
        <br>2.1 The Tsixi have the right to change the terms of this Agreement, if necessary, and notify the relevant page. You can consult the latest version of the agreement in terms of The Tsixi of game-related pages.
        <br>2.2 after the change in the terms of this agreement, if you continue to use The Tsixi service game, after you have accepted the change of the agreement shall be deemed to be. If you do not accept change after the agreement, shall stop using The Tsixi game service.
        <br>3、 The game account.
        <br>3.1 You fully understand and agree that the Tsixi will be in accordance with the relevant national requirements apply your real-name registration information to anti-addiction system, the Tsixi may according to your real name registration information whether you year full 18 one full year of life, to determine whether the fatigue limit to your game account.
        <br>3.2 You should keep your game account, you should know your game account stolen after immediately notify the Tsixi.
        <br>3.3 You fully understands and agrees that, in order to improve the safety level of the Tsixi of game service, the Tsixi shall have the right to apply the relevant technology or software to the Tsixi of the game, but the Tsixi does not guarantee that these security measures can be completely put an end to the game account number is stolen by others or the risk of loss.
        <br>3.4 You fully understand and agree that if the Tsixi in accordance with the relevant business rules limit, freeze, or terminate the use of your game accounts, could lead to your game account data and relevant information to be deleted, as well as the relevant rights and interests of the loss, the loss shall be borne by you, the Tsixi does not undertake any responsibility.
        <br>3.5 You fully understands and agrees that, for the efficient use of com.u8.server resources, if you are not used for a long time game account login the Tsixi, the Tsixi shall have the right to inspect need, in the case of advance notice, for the account and the account of game data and relevant information to delete disposal measures, such as the disposal can cause you to lose the game account under the relevant rights and interests of, one thousand line does not undertake any responsibility.
        <br>4、The user information collection, use and protect
        <br>4.1 You agree and authorize the Tsixi to fulfill the purpose of this agreement to collect your user information, this information includes the information you have registered in the real-name registration system, you game under the account of the data and other Tsixi .you use for the process of game service provided to the Tsixi or the Tsixi based on security, user experience optimization considering the need to collect information, the company of your user information collection will be in compliance with the provisions of the relevant laws.
        <br>4.2 You fully understand and agree that to provide you with better service Tsixi, the Tsixi of your user information can be submitted to its affiliates, and the Tsixi shall have the right to own or through a third party to organize your user information, statistics, analysis and use.
        <br>4.3You fully understand and agree that the Tsixi or the third party cooperation can according to your user information, via text message, telephone, E-mail and a variety of ways to provide you information about the Tsixi of game activities, the promotion of information and other kinds of information.
        <br>4.4 The Tsixi guarantee is not closed to the public or provide your personal information to any third party, except there are one of the following circumstances:
        <br>(1) public or provide the related information before your permission;
        <br>(2) according to the provisions of laws or policies and public or provide;
        <br>(3) the only public or provide your personal information, can provide you need the Tsixi of game service;
        <br>(4) according to the national public power authority, or provide;
        <br>(5) according to the other provisions of this agreement contract or provide publicly.
        <br>5 Game service
        <br>5.1 In your implementation of this agreement and the relevant laws and regulations under the premise of the Tsixi to give you a personal, non-transferable, and non-exclusive license to use the game service company. You only can be used for non-commercial purposeTsixi game service, including:
        <br>(1) Receiving, download, install, start, upgrade, login, display, operation and/or screenshots the Tsixi of the game;
        <br>(2) Create characters, set net, refer to the rules of the game, user profiles, games, game results, open game room, setting up the parameters, the purchase, use game props in the game, game equipment, COINS, etc., using the chat function, social sharing function;
        <br>(3) The use of the Tsixi support and allow the other one or several functions.
        <br>5.2 When you use the Tsixi 's gaming service process shall not in any way without permission to others to disseminate the company recorded game content, including not use any third-party software for network communication，etc.
        <br>5.3 Under the condition of the game in the form of software company provide, the game in the use of the Tsix and the Tsix game service should also satisfy the requirements of article i6 of this agreement about software license.
        <br>5.4 This and other provisions of this agreement all rights not expressly authorized other still retained by the Tsixi, when you in the exercise of these rights must be another the Tsixi of written permission.
        <br>5.5 If found the Tsixi or receive others to report complaints or user violates this agreement, the Tsixi shall have the right to delete the related content at any time without further notice, and regard plot behavior on the account including but not limited to, warning, restrict or ban the use of all or part of the function, the account banned until cancellation penalty, and deal with the results announcement.
        <br>5.6 You fully understand and agree that the Tsixi shall be entitled to in accordance with the reasonable judgment of violation of the relevant laws and regulations or the provisions of this agreement for punishment, in violation of any user to take appropriate legal action, according to the laws and regulations keep relevant information reported to the relevant departments, such as user alone shall bear all the legal responsibilities arising therefrom.
        <br>5.7 You fully understand and agree that, because of you, in violation of the provisions of the terms of this agreement or the relevant service, cause or generate any claims of third party claims, demands or loss, you should undertake liability independently; The Tsixi suffers any loss, you also shall pay for it.
        <br>5.8 You fully understand and agree that game props, equipment, and the game currency is part of the the Tsixi game service, such as  in the permission to you under this agreement and obtain the right to use. You buy, use game props, equipment, and money should follow this agreement such as game, game specific rules requirements; At the same time, the game props, equipment, game currency such as the limitation that may be effective, if you have not used the prescribed period of validity, in addition to the force majeure or can be attributable to thousands of lines of reason, once the expiration, will be null and void automatically.
        <br>5.9 You fully understand and agree that: to create a fair game environment, health, in the process of you use the Tsixi game service, the Tsixi shall have the right to through technical means knowing your random storage memory terminal equipment and procedures related the Tsixi games running at the same time. Once found any unauthorized, harm the normal running of the processes associated with the Tsixi of game service, Tsixi will collect all related information and take reasonable measures to crack down.
        <br>5.10 You fully understand and agree that in order to ensure that you and other users of the game speed, the Tsixi shall have the right to transfer or regularly cleared the Tsixi game some of the passing game data stored on the com.u8.server.
        <br>511 The Tsixi in accordance with the relevant laws and regulations and the provisions of this agreement, and take concrete and effective measures to protect minors in the use of the Tsixi of game service in the process of the legitimate rights and interests, including the possible technical measures, prohibit minors to contact is not suitable for the game or the function of play time, restrict minors, prevention of Internet addiction among minors. As part of the rules of the game, the Tsixi will also be released at the appropriate location the Tsixi user guide and the warning message, including the game content is introduced, and the correct use of the method and the method of prevent the harm of the game. All users of minors should be under the guidance of legal guardians carefully read and comply with these guidelines and instructions; Other players in the process of use the Tsixi game service should avoid to release, any detrimental to the health of minors content, create a healthy environment to play together.
        <br>6、Software license
        <br>6.1 Use the Tsixi game service may need to download and install the software, you can directly from the Tsixi of related web site, the software can also get the Tsixi of authorized third party can get it. If you get the Tsixi from the Tsixi without being authorized third party line or with the Tsixi game with the same name, will be treated as you without authorization by the the Tsixi, the Tsixi there is no guarantee that the game can normal use, and therefore shall not be responsible for the damage caused by for you.
        <br>6.2 The Tsixi may be different terminal device or operating system have developed different versions of the software, including but not limited to Windows, ios, android, Windows phone, symbian, blackberry, and other application version, you should choose to download the appropriate version according to actual condition for installation, download the installer, you need to follow the steps the program prompts installed correctly.
        <br>6.3 If the Tsixi provided in the form of software, games of the Tsixi to give you a personal, non-transferable, and non-exclusive license. You can only for non-commercial purposes in a single terminal devices to download, install, login, using the one thousand games.
        <br>6.4 To provide more high quality, safe service, line one thousand during software installation may recommend that you install other software, you can choose to install or not installed.
        <br>6.5 If you do not need to use the software or the need to install a new version, to be unloaded. If you are willing to help the Tsixi to improve products and services, please let us know the cause of the unloading.
        <br>6.6 In order to guarantee the safety of the Tsixi game service and function consistency, the Tsixi shall have the right to without special notice to you to update the software, or change or effect of the software part of the function limit.
        <br>6.7 The new version is released, the old version of the software may not be able to use. The Tsixi do not continue to ensure that the old version software available and the corresponding customer service, please feel free to check and download the latest version.
        <br>7、The user behavior specification
        <br>7.1 You fully understand and agree that you must be responsible for all actions under your game account, you can publish any content and the resulting consequences. You deal with the Tsixi of the game's content on their own judgment, and shall assume the caused by using the Tsixi game service all risks, including over the Tsixi of game content dependence on correctness, completeness, or usefulness of risk. The Tsixi rows cannot and will not for any loss or damage caused by the risk responsibility.
        <br>7.2 In addition to you, according to the stipulation of this agreement to use The Tsixi game services, shall not make any The Tsixi games of intellectual property rights infringement, or other damaging the Tsixi or other lawful rights and interests of a third party.
        <br>7.3 Unless the law or The Tsixi rows written permission, you may not engage in the following actions:
        <br>(1) Delete the game on a copy of the software and its information about copyright;
        <br>(2) The game software reverse engineering, reverse assemble, reverse compile, or otherwise attempt to find the software source code;
        <br>(3) The game software, scanning probe, testing, to detect, discovery, locate the possible bugs or weakness;
        <br>(4) In the process of game software or software running to release to any terminal data in memory, in the process of the software to run the client and com.u8.server interaction data, as well as the necessary Software to run system data, to copy, modify, add, delete, articulated run or create any derivative works, including but not limited to, the use of plug-in, plugins or the third party authorized by legal tools/services access software and related systems;
        <br>(5) To modify or counterfeit software instructions and data in operation, and the function of the increase, cut, changes in software or running effect, or will be used for the purpose of software, the method for operation or transmission to the public, whether the behavior for commercial purposes;
        <br>(6) Through the the Tsixi of development, and authorized by the third party software, plug-ins, plugins, system, using the one thousand game and the Tsixi of the service, or production, distribution, transmission of the Tsixi of development, the authorized third party software, plug-ins, plugins, system;
        <br>(7) For the game to the content of the the Tsixi with intellectual property rights to use, lease, lend, copy, modify, links, reprints, compiled and published, publishing, establish mirror sites, etc.;
        <br>(8) To build about the Tsixi of the mirrors of the game, or web page snapshot (abstinence), or using erect com.u8.server, to provide others with the Tsixi of game service is exactly the same or similar services;
        <br>(9) To isolate any part of the the Tsixi of game used alone, or other do not accord with the use of this agreement;
        <br>(10) Using the Tsixi of the game's name, trademark or other intellectual property rights;
        <br>(11) Other ACTS of the express authorization without the Tsixi.
        <br>7.4 You are in the process of use the Tsixi game service behavior, the Tsixi to see if the circumstances are serious, according to the provisions of this agreement and the relevant rules of the game, to temporarily or permanently prohibited to login, you delete the game account number and the data, delete information processing measures, such as if the circumstances are serious will be handed over to the relevant administrative authorities shall be given administrative punishment, or criminal responsibility shall be investigated for your:
        <br>(1) Suggests that in some way or pretend the Tsixi of internal staff or some special identity, in an attempt to get unfair interests or influence the behavior of other users' rights and interests;
        <br>(2) In the Tsixi of game using illegal or inappropriate words, characters, etc., including for character named;
        <br>(3) In any way damage the Tsixi games or affect the Tsixi game service;
        <br>(4) Various kinds of illegal plug-ins behavior;
        <br>(5) The spread of illegal or improper speech information;
        <br>(6) Theft account Numbers game, game items;
        <br>(7) Unauthorized trading game account.
        <br>(8) To secretly game props, equipment, such as game currency trading;
        <br>(9) Other widely recognized in the industry of misconduct, whether is already in this agreement or the rules of the game or explicitly specified.
        <br>8、 Disclaimer
        <br>The Tsixi game service “as is” available to you. The Tsixi does not guarantee that: The Tsixi game service without errors and will not interrupt, all defects have been corrected, or the Tsixi of game service will not be a virus or any other factors. Unless there is a law stipulates that Tsixi in the clear statement does not undertake any express or implied guarantee responsibilities, including but not limited to performance, applicability of The Tsixi game service guarantee or non-infringement.
        <br>8.2 In any case, the Tsixi is wrong of you as a result of force majeure in the use of the Tsixi of responsibility for any loss or damage sustained during the process of game service. Such force majeure event including but not limited to the national laws, regulations, policies and the orders of the state organ or other such as earthquake, flood, snow disaster, fire, tsunami, typhoon, strikes, wars, and other unpredictable, unavoidable and insurmountable event.
        <br>8.3 The Tsixi can be independently decided to change at any time, terminate, suspend your use of any the Tsixi game service, without any advance notice to you, but according to laws and regulations and The Tsixi of appointment or you need to notice in advance. If your behavior in violation of the provisions of the laws and regulations or the provisions of this agreement, the Tsixi in accordance with the relevant provisions to terminate, suspend your use of any service the Tsixi, the Tsixi without any liability to you, and shall have the right to require you to undertake the corresponding responsibility.
        <br>8.4 The Tsixi games may because of game software BUG, version updates defects, third-party virus attacks or any other factors lead to your game characters, props, game equipment and currency account data such as an exception occurs. In the cause of the abnormal data has not been found before, the Tsixi has the right to temporarily freeze the game account number; If find out abnormal data is abnormal behavior game, the Tsixi shall have the right to recover the account data to the exception occurs before the original state (including recovery of transferred data to a third party), and the Tsixi do not need to take any responsibility to you.
        <br>8.5 The Tsixi of unauthorized by buying from any third party, you accept the gift or other ways to obtain account Numbers game, game props, equipment, such as game currency, the Tsixi are not responsible for the behavior of the third party transactions, and not accepted by any third party trade dispute and complaint.
        <br>8.6 You fully understand that each objective conditions between different operating system, the objective conditions are not caused by exposure to the Tsixi, which could lead to your top-up and game data in an operating system cannot be successfully transferred to another operating system. Since you are in a different system to switch and game top-up the losses resulting from the risk of data loss shall be borne by your own, the Tsixi shall not be required to undertake any responsibility.
        <br>8.7 you fully understand to: the Tsixi of the game will be forced into play area or set play, if you do not agree to mandatory against, please don't enter the game or the area; You enter, the play will be deemed to agree to and accept the consequences.
        <br>9、Intellectual property
        <br>9.1 The Tsixi of the holder of the intellectual property right of the game. The Tsixi games of all copyright, trademarks, patents, trade secrets and other intellectual property rights, and all information related to The Tsixi game content (including text, images, audio, video, graphics, interface design, layout, framework and relevant data or electronic documents, etc.) are affected by the laws and regulations of the People's Republic of China and the relevant international treaties to protect, the Tsixi have the intellectual property rights, but the obligee's rights in accordance with the law shall be excepted.
        <br>9.2 When you use game services generated game data ownership and all intellectual property rights owned the Tsixi, the Tsixi have the right to dispose of the game data.
        <br>9.3 The Tsixi games may involve a third party intellectual property rights, and such third party for you based on this agreement in the Tsixi of the game of the request the use of such intellectual property rights, thousands will inform this requirement to you in the proper manner, you shall abide by it.
        <br>10 Regulatory compliance with local laws
        <br>10.1 You shall abide by local laws and regulations related to the use of the Tsixi in service during the game, and respect local customs and ethics. If you are in violation of local laws, regulations or ethical customs, you should assume responsibility for independence.
        <br>10.2 You should avoid the use of the Tsixi of game services leaving the Tsixi involved in the political and public events, otherwise the Tsixi right to suspend or terminate your service.
        <br>11、Privacy policy
        <br>Protect users' personal information and privacy is the Tsixi of a basic principle. Except as otherwise provided in this agreement, the Tsixi of game service will follow one thousand released by the unity of the related http://www.tsixi.com/privacy.html privacy policy
        <br>12、Jurisdiction and applicable law
        <br>12.1 This agreement is signed to the high-tech zone of chengdu in sichuan province of the People's Republic of China.
        <br>12.2 The establishment of this agreement, to take effect, implementation, interpretation and dispute resolution, applicable law of the People's Republic of China mainland (not including the conflict of laws).
        <br>12.3 If you and the Tsixi rows for this agreement any dispute or controversy between first shall be amicably settled through negotiation. If consultation fails, you agree to the dispute or disputes submitted to, this agreement is signed under the jurisdiction of the people's court that has jurisdiction.
        <br>12.4 The title of all terms and conditions of this agreement is only convenient for reading, in and of itself has no actual meaning, cannot be used as the basis for meaning interpretation of this agreement.
        <br>12.5 The terms of this agreement regardless of what kind of part is invalid, because the rest of the terms is still valid and binding upon the parties.
        <br>13、Other
        <br>13.1 According to the state general administration of press and publication about health advice of the game, the Tsixi of remind you: boycott bad games, refused to piracy game; Pay attention to self-protection, beware deceived; The moderate game profit brain, addicting games killed the cat.
        <br>13.2 If you have comments or suggestions to this agreement or the Tsixi game services, you can contact the customer service department of the Tsixi, we will give you the necessary help.
        <br>TsixiCo.,Ltd
    </div>
    <div class="row">
        <a class="btn btn-info btn-block" href="<%=basePath%>/login.jsp">${identify_text_18}</a>
    </div>
</div>
</body>
</html>
