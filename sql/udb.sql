/*
TSDK 数据库初始化SQL 需要在运行程序前先用MYSQL运行以下语句
由于uorder_bak 表使用了请在运行前更新日期
*/

SET FOREIGN_KEY_CHECKS=0;

/*
  该表为存储过程运行的日志表
*/
DROP TABLE IF EXISTS `proce_log`;
CREATE TABLE `proce_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `type` varchar(11) DEFAULT NULL,
  `eventTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*
  该表为自渠道配置表
*/
DROP TABLE IF EXISTS `self_channel_config`;
CREATE TABLE `self_channel_config` (
  `configID` int(11) NOT NULL,
  `channelID` int(11) DEFAULT NULL,
  `chargeChannelConfig` int(11) DEFAULT NULL,
  `msgClass` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `openLogin` int(11) DEFAULT NULL,
  `openRegister` int(11) DEFAULT NULL,
  PRIMARY KEY (`configID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为自渠道用户表 id为分区字段 一共分200区 每个区有100 0000 个分片
*/
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appID` int(11) NOT NULL,
  `channelID` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `psw` varchar(32) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `realPsw` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `USER_NAME` (`username`) USING BTREE,
  KEY `E_MAIL` (`email`) USING BTREE,
  KEY `t_user_password` (`psw`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
PARTITION BY RANGE (`id`)(
  PARTITION p1 VALUES LESS THAN (1000000),
  PARTITION p2 VALUES LESS THAN (2000000),
  PARTITION p3 VALUES LESS THAN (3000000),
  PARTITION p4 VALUES LESS THAN (4000000),
  PARTITION p5 VALUES LESS THAN (5000000),
  PARTITION p6 VALUES LESS THAN (6000000),
  PARTITION p7 VALUES LESS THAN (7000000),
  PARTITION p8 VALUES LESS THAN (8000000),
  PARTITION p9 VALUES LESS THAN (9000000),
  PARTITION p10 VALUES LESS THAN (10000000),
  PARTITION p11 VALUES LESS THAN (11000000),
  PARTITION p12 VALUES LESS THAN (12000000),
  PARTITION p13 VALUES LESS THAN (13000000),
  PARTITION p14 VALUES LESS THAN (14000000),
  PARTITION p15 VALUES LESS THAN (15000000),
  PARTITION p16 VALUES LESS THAN (16000000),
  PARTITION p17 VALUES LESS THAN (17000000),
  PARTITION p18 VALUES LESS THAN (18000000),
  PARTITION p19 VALUES LESS THAN (19000000),
  PARTITION p20 VALUES LESS THAN (20000000),
  PARTITION p21 VALUES LESS THAN (21000000),
  PARTITION p22 VALUES LESS THAN (22000000),
  PARTITION p23 VALUES LESS THAN (23000000),
  PARTITION p24 VALUES LESS THAN (24000000),
  PARTITION p25 VALUES LESS THAN (25000000),
  PARTITION p26 VALUES LESS THAN (26000000),
  PARTITION p27 VALUES LESS THAN (27000000),
  PARTITION p28 VALUES LESS THAN (28000000),
  PARTITION p29 VALUES LESS THAN (29000000),
  PARTITION p30 VALUES LESS THAN (30000000),
  PARTITION p31 VALUES LESS THAN (31000000),
  PARTITION p32 VALUES LESS THAN (32000000),
  PARTITION p33 VALUES LESS THAN (33000000),
  PARTITION p34 VALUES LESS THAN (34000000),
  PARTITION p35 VALUES LESS THAN (35000000),
  PARTITION p36 VALUES LESS THAN (36000000),
  PARTITION p37 VALUES LESS THAN (37000000),
  PARTITION p38 VALUES LESS THAN (38000000),
  PARTITION p39 VALUES LESS THAN (39000000),
  PARTITION p40 VALUES LESS THAN (40000000),
  PARTITION p41 VALUES LESS THAN (41000000),
  PARTITION p42 VALUES LESS THAN (42000000),
  PARTITION p43 VALUES LESS THAN (43000000),
  PARTITION p44 VALUES LESS THAN (44000000),
  PARTITION p45 VALUES LESS THAN (45000000),
  PARTITION p46 VALUES LESS THAN (46000000),
  PARTITION p47 VALUES LESS THAN (47000000),
  PARTITION p48 VALUES LESS THAN (48000000),
  PARTITION p49 VALUES LESS THAN (49000000),
  PARTITION p50 VALUES LESS THAN (50000000),
  PARTITION p51 VALUES LESS THAN (51000000),
  PARTITION p52 VALUES LESS THAN (52000000),
  PARTITION p53 VALUES LESS THAN (53000000),
  PARTITION p54 VALUES LESS THAN (54000000),
  PARTITION p55 VALUES LESS THAN (55000000),
  PARTITION p56 VALUES LESS THAN (56000000),
  PARTITION p57 VALUES LESS THAN (57000000),
  PARTITION p58 VALUES LESS THAN (58000000),
  PARTITION p59 VALUES LESS THAN (59000000),
  PARTITION p60 VALUES LESS THAN (60000000),
  PARTITION p61 VALUES LESS THAN (61000000),
  PARTITION p62 VALUES LESS THAN (62000000),
  PARTITION p63 VALUES LESS THAN (63000000),
  PARTITION p64 VALUES LESS THAN (64000000),
  PARTITION p65 VALUES LESS THAN (65000000),
  PARTITION p66 VALUES LESS THAN (66000000),
  PARTITION p67 VALUES LESS THAN (67000000),
  PARTITION p68 VALUES LESS THAN (68000000),
  PARTITION p69 VALUES LESS THAN (69000000),
  PARTITION p70 VALUES LESS THAN (70000000),
  PARTITION p71 VALUES LESS THAN (71000000),
  PARTITION p72 VALUES LESS THAN (72000000),
  PARTITION p73 VALUES LESS THAN (73000000),
  PARTITION p74 VALUES LESS THAN (74000000),
  PARTITION p75 VALUES LESS THAN (75000000),
  PARTITION p76 VALUES LESS THAN (76000000),
  PARTITION p77 VALUES LESS THAN (77000000),
  PARTITION p78 VALUES LESS THAN (78000000),
  PARTITION p79 VALUES LESS THAN (79000000),
  PARTITION p80 VALUES LESS THAN (80000000),
  PARTITION p81 VALUES LESS THAN (81000000),
  PARTITION p82 VALUES LESS THAN (82000000),
  PARTITION p83 VALUES LESS THAN (83000000),
  PARTITION p84 VALUES LESS THAN (84000000),
  PARTITION p85 VALUES LESS THAN (85000000),
  PARTITION p86 VALUES LESS THAN (86000000),
  PARTITION p87 VALUES LESS THAN (87000000),
  PARTITION p88 VALUES LESS THAN (88000000),
  PARTITION p89 VALUES LESS THAN (89000000),
  PARTITION p90 VALUES LESS THAN (90000000),
  PARTITION p91 VALUES LESS THAN (91000000),
  PARTITION p92 VALUES LESS THAN (92000000),
  PARTITION p93 VALUES LESS THAN (93000000),
  PARTITION p94 VALUES LESS THAN (94000000),
  PARTITION p95 VALUES LESS THAN (95000000),
  PARTITION p96 VALUES LESS THAN (96000000),
  PARTITION p97 VALUES LESS THAN (97000000),
  PARTITION p98 VALUES LESS THAN (98000000),
  PARTITION p99 VALUES LESS THAN (99000000),
  PARTITION p100 VALUES LESS THAN (100000000),
  PARTITION p101 VALUES LESS THAN (101000000),
  PARTITION p102 VALUES LESS THAN (102000000),
  PARTITION p103 VALUES LESS THAN (103000000),
  PARTITION p104 VALUES LESS THAN (104000000),
  PARTITION p105 VALUES LESS THAN (105000000),
  PARTITION p106 VALUES LESS THAN (106000000),
  PARTITION p107 VALUES LESS THAN (107000000),
  PARTITION p108 VALUES LESS THAN (108000000),
  PARTITION p109 VALUES LESS THAN (109000000),
  PARTITION p110 VALUES LESS THAN (110000000),
  PARTITION p111 VALUES LESS THAN (111000000),
  PARTITION p112 VALUES LESS THAN (112000000),
  PARTITION p113 VALUES LESS THAN (113000000),
  PARTITION p114 VALUES LESS THAN (114000000),
  PARTITION p115 VALUES LESS THAN (115000000),
  PARTITION p116 VALUES LESS THAN (116000000),
  PARTITION p117 VALUES LESS THAN (117000000),
  PARTITION p118 VALUES LESS THAN (118000000),
  PARTITION p119 VALUES LESS THAN (119000000),
  PARTITION p120 VALUES LESS THAN (120000000),
  PARTITION p121 VALUES LESS THAN (121000000),
  PARTITION p122 VALUES LESS THAN (122000000),
  PARTITION p123 VALUES LESS THAN (123000000),
  PARTITION p124 VALUES LESS THAN (124000000),
  PARTITION p125 VALUES LESS THAN (125000000),
  PARTITION p126 VALUES LESS THAN (126000000),
  PARTITION p127 VALUES LESS THAN (127000000),
  PARTITION p128 VALUES LESS THAN (128000000),
  PARTITION p129 VALUES LESS THAN (129000000),
  PARTITION p130 VALUES LESS THAN (130000000),
  PARTITION p131 VALUES LESS THAN (131000000),
  PARTITION p132 VALUES LESS THAN (132000000),
  PARTITION p133 VALUES LESS THAN (133000000),
  PARTITION p134 VALUES LESS THAN (134000000),
  PARTITION p135 VALUES LESS THAN (135000000),
  PARTITION p136 VALUES LESS THAN (136000000),
  PARTITION p137 VALUES LESS THAN (137000000),
  PARTITION p138 VALUES LESS THAN (138000000),
  PARTITION p139 VALUES LESS THAN (139000000),
  PARTITION p140 VALUES LESS THAN (140000000),
  PARTITION p141 VALUES LESS THAN (141000000),
  PARTITION p142 VALUES LESS THAN (142000000),
  PARTITION p143 VALUES LESS THAN (143000000),
  PARTITION p144 VALUES LESS THAN (144000000),
  PARTITION p145 VALUES LESS THAN (145000000),
  PARTITION p146 VALUES LESS THAN (146000000),
  PARTITION p147 VALUES LESS THAN (147000000),
  PARTITION p148 VALUES LESS THAN (148000000),
  PARTITION p149 VALUES LESS THAN (149000000),
  PARTITION p150 VALUES LESS THAN (150000000),
  PARTITION p151 VALUES LESS THAN (151000000),
  PARTITION p152 VALUES LESS THAN (152000000),
  PARTITION p153 VALUES LESS THAN (153000000),
  PARTITION p154 VALUES LESS THAN (154000000),
  PARTITION p155 VALUES LESS THAN (155000000),
  PARTITION p156 VALUES LESS THAN (156000000),
  PARTITION p157 VALUES LESS THAN (157000000),
  PARTITION p158 VALUES LESS THAN (158000000),
  PARTITION p159 VALUES LESS THAN (159000000),
  PARTITION p160 VALUES LESS THAN (160000000),
  PARTITION p161 VALUES LESS THAN (161000000),
  PARTITION p162 VALUES LESS THAN (162000000),
  PARTITION p163 VALUES LESS THAN (163000000),
  PARTITION p164 VALUES LESS THAN (164000000),
  PARTITION p165 VALUES LESS THAN (165000000),
  PARTITION p166 VALUES LESS THAN (166000000),
  PARTITION p167 VALUES LESS THAN (167000000),
  PARTITION p168 VALUES LESS THAN (168000000),
  PARTITION p169 VALUES LESS THAN (169000000),
  PARTITION p170 VALUES LESS THAN (170000000),
  PARTITION p171 VALUES LESS THAN (171000000),
  PARTITION p172 VALUES LESS THAN (172000000),
  PARTITION p173 VALUES LESS THAN (173000000),
  PARTITION p174 VALUES LESS THAN (174000000),
  PARTITION p175 VALUES LESS THAN (175000000),
  PARTITION p176 VALUES LESS THAN (176000000),
  PARTITION p177 VALUES LESS THAN (177000000),
  PARTITION p178 VALUES LESS THAN (178000000),
  PARTITION p179 VALUES LESS THAN (179000000),
  PARTITION p180 VALUES LESS THAN (180000000),
  PARTITION p181 VALUES LESS THAN (181000000),
  PARTITION p182 VALUES LESS THAN (182000000),
  PARTITION p183 VALUES LESS THAN (183000000),
  PARTITION p184 VALUES LESS THAN (184000000),
  PARTITION p185 VALUES LESS THAN (185000000),
  PARTITION p186 VALUES LESS THAN (186000000),
  PARTITION p187 VALUES LESS THAN (187000000),
  PARTITION p188 VALUES LESS THAN (188000000),
  PARTITION p189 VALUES LESS THAN (189000000),
  PARTITION p190 VALUES LESS THAN (190000000),
  PARTITION p191 VALUES LESS THAN (191000000),
  PARTITION p192 VALUES LESS THAN (192000000),
  PARTITION p193 VALUES LESS THAN (193000000),
  PARTITION p194 VALUES LESS THAN (194000000),
  PARTITION p195 VALUES LESS THAN (195000000),
  PARTITION p196 VALUES LESS THAN (196000000),
  PARTITION p197 VALUES LESS THAN (197000000),
  PARTITION p198 VALUES LESS THAN (198000000),
  PARTITION p199 VALUES LESS THAN (199000000),
  PARTITION p200 VALUES LESS THAN (200000000)
);
/*
  该表为管理用户表
*/
DROP TABLE IF EXISTS `uadmin`;
CREATE TABLE `uadmin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `permission` int(11) DEFAULT NULL,
  `pAdminID` int(11) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
INSERT INTO `uadmin` VALUES ('1', 'admin', '7bccfde7714a1ebadf06c5f4cea752c1', '0', '0', '开发人员');
/*
  该表为渠道表
*/
DROP TABLE IF EXISTS `uchannel`;
CREATE TABLE `uchannel` (
  `channelID` int(11) NOT NULL,
  `appID` int(11) NOT NULL,
  `cpAppID` varchar(255) DEFAULT NULL,
  `cpAppKey` text,
  `cpAppSecret` text,
  `cpID` varchar(255) DEFAULT NULL,
  `cpPayID` varchar(255) DEFAULT NULL,
  `cpPayKey` text,
  `cpPayPriKey` text,
  `masterID` int(11) NOT NULL,
  `cpConfig` varchar(1024) DEFAULT NULL,
  `createAdminID` int(11) NOT NULL,
  `goodsConfigID` int(11) DEFAULT NULL,
  `channelName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`channelID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为渠道商表
*/
DROP TABLE IF EXISTS `uchannelmaster`;
CREATE TABLE `uchannelmaster` (
  `masterID` int(11) NOT NULL,
  `authUrl` varchar(1024) DEFAULT NULL,
  `masterName` varchar(255) DEFAULT NULL,
  `sdkName` varchar(255) DEFAULT NULL,
  `nameSuffix` varchar(255) DEFAULT NULL,
  `payCallbackUrl` varchar(1024) DEFAULT NULL,
  `verifyClass` varchar(1024) DEFAULT NULL,
  `orderUrl` varchar(1024) DEFAULT NULL,
  `validField` varchar(1024) DEFAULT NULL,
  `selfChannel` int(11) DEFAULT NULL,
  PRIMARY KEY (`masterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `uchannelmaster` VALUES ('1', 'http://passport_i.25pp.com:8080/account?tunnel-command=2852126760', 'PP助手', 'pp', '.pp', 'http://localhost:8080/uc/payCallback', 'com.u8.server.sdk.pp.PPSDK', '', 'cpAppID, cpAppKey, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('2', 'http://ngsdk.d.cn/api/cp/checkToken', '当乐', 'downjoy', '.dl', 'http://localhost:8080/downjoy/payCallback', 'com.u8.server.sdk.downjoy.DownjoySDK', '', 'cpAppID, cpAppKey, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('3', 'https://openapi.360.cn/user/me', '360', 'qihoo360', '.qihoo', 'http://192.168.3.32:8080/qihoo360/payCallback', 'com.u8.server.sdk.qihoo360.Qihoo360SDK', null, null, '0');
INSERT INTO `uchannelmaster` VALUES ('4', 'http://querysdkapi.91.com/CpLoginStateQuery.ashx', '百度', 'baidu', '.baidu', 'http://localhost:8080/baidu/payCallback', 'com.u8.server.sdk.baidu.BaiduSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('5', 'http://mis.migc.xiaomi.com/api/biz/service/verifySession.do', '小米', 'xiaomi', '.xiaomi', 'http://localhost:8080/xiaomi/payCallback', 'com.u8.server.sdk.xiaomi.XiaoMiSDK', '', 'cpAppID, cpAppKey, cpAppSecret', '0');
INSERT INTO `uchannelmaster` VALUES ('6', 'http://m.4399api.com/openapi/oauth-check.html', '4399', 'm4399', '.m4399', 'http://localhost:8080/m4399/payCallback', 'com.u8.server.sdk.m4399.M4399SDK', '', 'cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('7', '', 'oppo', 'oppo', '.oppo', 'http://localhost:8080/oppo/payCallback', 'com.u8.server.sdk.oppo.OppoSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('8', 'https://usrsys.inner.bbk.com/auth/user/info', 'ViVo', 'vivo', '.vivo', 'http://localhost:8080/vivo/payCallback', 'com.u8.server.sdk.vivo.VivoSDK', 'https://pay.vivo.com.cn/vcoin/trade', null, '0');
INSERT INTO `uchannelmaster` VALUES ('9', 'http://sdk.muzhiwan.com/oauth2/getuser.php', '拇指玩', 'muzhiwan', '.mzw', 'http://localhost:8080/muzhiwan/payCallback', 'com.u8.server.sdk.muzhiwan.MuZhiWanSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('10', 'https://pay.wandoujia.com/api/uid/check', '豌豆荚', 'wandoujia', '.wdj', 'http://localhost:8080/wandoujia/payCallback', 'com.u8.server.sdk.wandoujia.WanDouJiaSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('11', 'http://passport.lenovo.com/interserver/authen/1.2/getaccountid', '联想', 'lenovo', '.lenovo', 'http://localhost:8080/lenovo/payCallback', 'com.u8.server.sdk.lenovo.LenovoSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('12', 'https://api.vmall.com/rest.php', '华为', 'huawei', '.huawei', 'http://localhost:8080/huawei/payCallback', 'com.u8.server.sdk.huawei.HuaWeiSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('13', 'http://api.appchina.com/appchina-usersdk/user/get.json', '应用汇', 'appchina', '.appchina', 'http://localhost:8080/appchina/payCallback', 'com.u8.server.sdk.appchina.AppChinaSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('14', 'http://server.cpo2o.com/Verify/login.html', '友游', 'cloudpoint', '.cloudpoint', 'http://localhost:8080/cloudpoint/payCallback', 'com.u8.server.sdk.cloudpoint.CloudPointSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('15', 'http://user.anzhi.com/web/api/sdk/third/1/queryislogin', '安智', 'anzhi', '.anzhi', 'http://localhost:8080/anzhi/payCallback', 'com.u8.server.sdk.anzhi.AnzhiSDK', '', 'cpAppKey, cpAppSecret', '0');
INSERT INTO `uchannelmaster` VALUES ('16', 'http://pay.mumayi.com/user/index/validation', '木蚂蚁', 'mumayi', '.mumayi', 'http://localhost:8080/mumayi/payCallback', 'com.u8.server.sdk.mumayi.MuMaYiSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('17', 'http://guopan.cn/gamesdk/verify', '叉叉助手', 'guopan', '.guopan', 'http://localhost:8080/guopan/payCallback', 'com.u8.server.sdk.guopan.GuoPanSDK', '', 'cpAppID, cpAppKey, cpAppSecret', '0');
INSERT INTO `uchannelmaster` VALUES ('18', 'http://ng.sdk.paojiao.cn/api/user/token.do', '泡椒', 'paojiao', '.paojiao', 'http://localhost:8080/paojiao/payCallback', 'com.u8.server.sdk.paojiao.PaoJiaoSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('19', 'https://api.game.meizu.com/game/security/checksession', '魅族', 'meizu', '.meizu', 'http://localhost:8080/meizu/payCallback', 'com.u8.server.sdk.meizu.MeizuSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('20', 'https://openapi.coolyun.com/oauth2/token', '酷派', 'coolpad', '.coolpad', 'http://localhost:8080/coolpad/payCallback', 'com.u8.server.sdk.coolpad.CoolPadSDK', 'http://pay.coolyun.com:6988/payapi/order', null, '0');
INSERT INTO `uchannelmaster` VALUES ('21', '', '偶玩', 'ouwan', '.ouwan', 'http://localhost:8080/ouwan/payCallback', 'com.u8.server.sdk.ouwan.OuWanSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('22', 'https://id.gionee.com/account/verify.do', '金立', 'jinli', '.am', 'http://localhost:8080/jinli/payCallback', 'com.u8.server.sdk.jinli.JinLiSDK', 'https://pay.gionee.com/order/create', null, '0');
INSERT INTO `uchannelmaster` VALUES ('23', 'https://pay.slooti.com/?r=auth/verify', 'Itools', 'itools', '.itools', 'http://localhost:8080/itools/payCallback', 'com.u8.server.sdk.itools.ItoolsSDK', '', 'cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('24', '', '自渠道 - 爱贝支付', '', '.tsixi', '', 'com.u8.server.sdk.selfSDK.TSDKIPay', 'http://117.79.227.178:9999/payapi/order', 'cpAppID, cpPayKey, cpPayPriKey', '1');
INSERT INTO `uchannelmaster` VALUES ('25', 'http://passport.xyzs.com/checkLogin.php', 'XY助手', 'xy', '.xy', 'http://localhost:8080/pay/xy/payCallback', 'com.u8.server.sdk.xy.XYSDK', '', 'cpAppID, cpAppKey, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('26', 'http://api.haimawan.com/index.php?m=api&a=validate_token', '海马', 'haima', '.haima', 'http://localhost:8080/pay/haima/payCallback', 'com.u8.server.sdk.haima.HaiMaSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('27', 'http://tgi.tongbu.com/api/LoginCheck.ashx', '同步推', 'tongbu', '.tongbu', 'http://localhost:8080/tongbu/pay/payCallback', 'com.u8.server.sdk.tongbu.TongbuSDK', '', 'cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('28', 'https://pay.i4.cn/member_third.action', '爱思', 'i4', '.i4', '/pay/aisi/payCallback', 'com.u8.server.sdk.aisi.i4SDK', '', 'cpAppID, cpAppKey, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('29', '', '91', '91', '.91', '', 'com.u8.server.sdk.baidu.BaiduSDK', '', null, '0');
INSERT INTO `uchannelmaster` VALUES ('30', 'http://f_signin.bppstore.com/loginCheck.php', '快用', 'kuaiyong', '.kuaiyong', '/pay/', 'com.u8.server.sdk.kuaiyong.KuaiyongSDK', '/pay/kuaiyong/payCallback', 'cpAppID, cpAppKey, cpPayID, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('31', 'http://sdk.g.uc.cn/cp/account.verifySession', 'UC', 'uc', '.uc', '', 'com.u8.server.sdk.uc.UCSDK', '', 'cpID, cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('32', 'http://api.sy.7k7k.com/index.php/user/checkUser', '7K7K', '7k', '.7k', '', 'com.u8.server.sdk.m7k7k.M7k7kSDK', '', 'cpID, cpAppID, cpAppKey, cpAppSecret, cpPayPriKey', '0');
INSERT INTO `uchannelmaster` VALUES ('33', 'http://android-api.ccplay.com.cn/api/v2/payment/checkUser', '虫虫游戏', 'cc', '.cc', '', 'com.u8.server.sdk.cc.CCSDK', '', 'cpID, cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('34', 'http://api.app.wan.sogou.com/api/v1/login/verify', '搜狗', 'sougou', '.shougou', '', 'com.u8.server.sdk.sogou.SogouSDK', '', 'cpAppID, cpAppKey, cpAppSecret, cpPayKey', '0');
INSERT INTO `uchannelmaster` VALUES ('35', '', '说玩', 'shuowan', '.shuowan', '', 'com.u8.server.sdk.shuowan.ShuoWanSDK', '', 'cpID, cpAppID, cpAppKey', '0');
INSERT INTO `uchannelmaster` VALUES ('36', '', '自渠道 - Google充值', '', '.tsixi', '', 'com.u8.server.sdk.selfSDK.TSDK', '', 'cpAppID, cpPayKey', '1');
INSERT INTO `uchannelmaster` VALUES ('37', '', '鱼丸', 'Yuwan', '.yuwan', '', 'com.u8.server.sdk.yuwan.YuwanSDK', '', 'cpID, cpAppID, cpAppKey, cpConfig', '0');
/*
  该表为充值渠道表
*/
DROP TABLE IF EXISTS `uchargechannel`;
CREATE TABLE `uchargechannel` (
  `id` int(11) NOT NULL,
  `chargeChannelName` varchar(255) DEFAULT NULL,
  `appID` varchar(255) DEFAULT NULL,
  `appKey` varchar(255) DEFAULT NULL,
  `appSecret` varchar(255) DEFAULT NULL,
  `config` varchar(255) DEFAULT NULL,
  `iconUrl` varchar(255) DEFAULT NULL,
  `scriptClass` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `adminID` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为游戏表
*/
DROP TABLE IF EXISTS `ugame`;
CREATE TABLE `ugame` (
  `appID` int(11) NOT NULL,
  `appkey` varchar(255) DEFAULT NULL,
  `appRSAPubKey` varchar(1024) DEFAULT NULL,
  `appRSAPriKey` varchar(1024) DEFAULT NULL,
  `createTime` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `payCallback` varchar(255) NOT NULL,
  `payCallbackDebug` varchar(255) DEFAULT NULL,
  `msdkPayCallback` varchar(255) DEFAULT NULL,
  `createAdminID` int(11) DEFAULT NULL,
  `logUrl` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`appID`,`payCallback`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为商品表
*/
DROP TABLE IF EXISTS `ugoods`;
CREATE TABLE `ugoods` (
  `goodsID` int(11) NOT NULL,
  `goodsName` varchar(255) DEFAULT NULL,
  `productID` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `moneyUnit` varchar(255) DEFAULT NULL,
  `sdkGoodsID` varchar(255) DEFAULT NULL,
  `goodsConfigID` int(11) DEFAULT NULL,
  `diamond` int(11) DEFAULT NULL,
  PRIMARY KEY (`goodsID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为商品配置表
*/
DROP TABLE IF EXISTS `ugoods_config`;
CREATE TABLE `ugoods_config` (
  `id` int(11) NOT NULL,
  `createAdminID` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为模块表
*/
DROP TABLE IF EXISTS `umodule`;
CREATE TABLE `umodule` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `url` text,
  `iconCls` varchar(255) DEFAULT NULL,
  `permission` int(11) NOT NULL,
  `leaf` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `umodule` VALUES ('1', '渠道配置', '/admin/channels', '/admin/index', 'fa-inbox', '2', '1', '10');
INSERT INTO `umodule` VALUES ('2', '商品配置', null, '/admin/goodsConfig/showGoods', 'fa-shopping-cart', '2', '1', '-1');
INSERT INTO `umodule` VALUES ('3', '游戏管理', '/admin/games', '/admin/games/showGames', 'fa-gamepad', '1', '1', '1');
INSERT INTO `umodule` VALUES ('4', '渠道商管理', '/admin/channelMaster', '/admin/channelMaster/showChannelMasters', 'fa-desktop', '0', '1', '10');
INSERT INTO `umodule` VALUES ('5', '玩家查询', '/admin/users', '/admin/users/showUsers', 'fa-search', '3', '1', '11');
INSERT INTO `umodule` VALUES ('6', '订单查询', null, '/admin/orders/showOrders', 'fa-files-o', '3', '1', '11');
INSERT INTO `umodule` VALUES ('7', '模块管理', '/admin/module', '/admin/module/showModule', 'fa-th-large', '0', '1', '12');
INSERT INTO `umodule` VALUES ('9', '用户管理', '/admin/manage', '/admin/manage/showAdmins', 'fa-users', '1', '1', '12');
INSERT INTO `umodule` VALUES ('10', '外部渠道管理', null, null, 'fa-outdent', '1', '0', '-1');
INSERT INTO `umodule` VALUES ('11', '查询', null, null, 'fa-search', '3', '0', '-1');
INSERT INTO `umodule` VALUES ('12', '系统管理', null, null, 'fa-gears', '0', '0', '-1');
INSERT INTO `umodule` VALUES ('13', '自渠道管理', null, null, 'fa-cube', '0', '0', '-1');
INSERT INTO `umodule` VALUES ('14', '充值渠道商管理', null, '/admin/chargeChannel/showChargeChannels', 'fa-shopping-cart', '0', '1', '10');
INSERT INTO `umodule` VALUES ('15', '功能配置', null, '/admin/self/showChargeChannels', 'fa-wrench', '0', '1', '13');
INSERT INTO `umodule` VALUES ('16', '充值渠道管理', null, '', 'fa-cart-plus', '0', '1', '13');
INSERT INTO `umodule` VALUES ('17', '游戏管理', null, '/admin/games/showGames', 'fa-gamepad', '0', '1', '12');
/*
  该表为订单表
*/
DROP TABLE IF EXISTS `uorder`;
CREATE TABLE `uorder` (
  `orderID` bigint(20) NOT NULL,
  `appID` int(11) NOT NULL,
  `channelID` int(11) NOT NULL,
  `channelOrderID` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `money` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `roleID` varchar(255) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `serverID` varchar(255) DEFAULT NULL,
  `serverName` varchar(255) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productDesc` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `diamond` int(11) DEFAULT NULL,
  PRIMARY KEY (`orderID`),
  UNIQUE KEY `ORDER_ID` (`orderID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  该表为订单备份表 , 该表没有主键 而且该表是以日期做分区 查询时需要带上日期才能达到最快速度
*/
DROP TABLE IF EXISTS `uorder_bak`;
CREATE TABLE `uorder_bak` (
  `orderID` bigint(20) NOT NULL,
  `appID` int(11) NOT NULL,
  `channelID` int(11) NOT NULL,
  `channelOrderID` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `money` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `roleID` varchar(255) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `serverID` varchar(255) DEFAULT NULL,
  `serverName` varchar(255) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productDesc` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `diamond` int(11) DEFAULT NULL,
  KEY `ORDER_BAK_ID` (`orderID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
PARTITION BY RANGE (to_days(createdTime))(
  PARTITION p1 VALUES LESS THAN (to_days("2017-02-01")+30),
  PARTITION p2 VALUES LESS THAN (to_days("2017-02-01")+60),
  PARTITION p3 VALUES LESS THAN (to_days("2017-02-01")+90),
  PARTITION p4 VALUES LESS THAN (to_days("2017-02-01")+120),
  PARTITION p5 VALUES LESS THAN (to_days("2017-02-01")+150),
  PARTITION p6 VALUES LESS THAN (to_days("2017-02-01")+180),
  PARTITION p7 VALUES LESS THAN (to_days("2017-02-01")+210),
  PARTITION p8 VALUES LESS THAN (to_days("2017-02-01")+240),
  PARTITION p9 VALUES LESS THAN (to_days("2017-02-01")+270),
  PARTITION p10 VALUES LESS THAN (to_days("2017-02-01")+300),
  PARTITION p11 VALUES LESS THAN (to_days("2017-02-01")+330),
  PARTITION p12 VALUES LESS THAN (to_days("2017-02-01")+360),
  PARTITION p13 VALUES LESS THAN (to_days("2017-02-01")+390),
  PARTITION p14 VALUES LESS THAN (to_days("2017-02-01")+420),
  PARTITION p15 VALUES LESS THAN (to_days("2017-02-01")+450),
  PARTITION p16 VALUES LESS THAN (to_days("2017-02-01")+480),
  PARTITION p17 VALUES LESS THAN (to_days("2017-02-01")+510),
  PARTITION p18 VALUES LESS THAN (to_days("2017-02-01")+540),
  PARTITION p19 VALUES LESS THAN (to_days("2017-02-01")+570),
  PARTITION p20 VALUES LESS THAN (to_days("2017-02-01")+600),
  PARTITION p21 VALUES LESS THAN (to_days("2017-02-01")+630),
  PARTITION p22 VALUES LESS THAN (to_days("2017-02-01")+660),
  PARTITION p23 VALUES LESS THAN (to_days("2017-02-01")+690),
  PARTITION p24 VALUES LESS THAN (to_days("2017-02-01")+720),
  PARTITION p25 VALUES LESS THAN (to_days("2017-02-01")+750),
  PARTITION p26 VALUES LESS THAN (to_days("2017-02-01")+780),
  PARTITION p27 VALUES LESS THAN (to_days("2017-02-01")+810),
  PARTITION p28 VALUES LESS THAN (to_days("2017-02-01")+840),
  PARTITION p29 VALUES LESS THAN (to_days("2017-02-01")+870),
  PARTITION p30 VALUES LESS THAN (to_days("2017-02-01")+900),
  PARTITION p31 VALUES LESS THAN (to_days("2017-02-01")+930),
  PARTITION p32 VALUES LESS THAN (to_days("2017-02-01")+960),
  PARTITION p33 VALUES LESS THAN (to_days("2017-02-01")+990),
  PARTITION p34 VALUES LESS THAN (to_days("2017-02-01")+1020),
  PARTITION p35 VALUES LESS THAN (to_days("2017-02-01")+1050),
  PARTITION p36 VALUES LESS THAN (to_days("2017-02-01")+1080),
  PARTITION p37 VALUES LESS THAN (to_days("2017-02-01")+1110),
  PARTITION p38 VALUES LESS THAN (to_days("2017-02-01")+1140),
  PARTITION p39 VALUES LESS THAN (to_days("2017-02-01")+1170),
  PARTITION p40 VALUES LESS THAN (to_days("2017-02-01")+1200),
  PARTITION p41 VALUES LESS THAN (to_days("2017-02-01")+1230),
  PARTITION p42 VALUES LESS THAN (to_days("2017-02-01")+1260),
  PARTITION p43 VALUES LESS THAN (to_days("2017-02-01")+1290),
  PARTITION p44 VALUES LESS THAN (to_days("2017-02-01")+1320),
  PARTITION p45 VALUES LESS THAN (to_days("2017-02-01")+1350),
  PARTITION p46 VALUES LESS THAN (to_days("2017-02-01")+1380),
  PARTITION p47 VALUES LESS THAN (to_days("2017-02-01")+1410),
  PARTITION p48 VALUES LESS THAN (to_days("2017-02-01")+1440),
  PARTITION p49 VALUES LESS THAN (to_days("2017-02-01")+1470),
  PARTITION p50 VALUES LESS THAN (to_days("2017-02-01")+1500),
  PARTITION p51 VALUES LESS THAN (to_days("2017-02-01")+1530),
  PARTITION p52 VALUES LESS THAN (to_days("2017-02-01")+1560),
  PARTITION p53 VALUES LESS THAN (to_days("2017-02-01")+1590),
  PARTITION p54 VALUES LESS THAN (to_days("2017-02-01")+1620),
  PARTITION p55 VALUES LESS THAN (to_days("2017-02-01")+1650),
  PARTITION p56 VALUES LESS THAN (to_days("2017-02-01")+1680),
  PARTITION p57 VALUES LESS THAN (to_days("2017-02-01")+1710),
  PARTITION p58 VALUES LESS THAN (to_days("2017-02-01")+1740),
  PARTITION p59 VALUES LESS THAN (to_days("2017-02-01")+1770),
  PARTITION p60 VALUES LESS THAN (to_days("2017-02-01")+1800),
  PARTITION p61 VALUES LESS THAN (to_days("2017-02-01")+1830),
  PARTITION p62 VALUES LESS THAN (to_days("2017-02-01")+1860),
  PARTITION p63 VALUES LESS THAN (to_days("2017-02-01")+1890),
  PARTITION p64 VALUES LESS THAN (to_days("2017-02-01")+1920),
  PARTITION p65 VALUES LESS THAN (to_days("2017-02-01")+1950),
  PARTITION p66 VALUES LESS THAN (to_days("2017-02-01")+1980),
  PARTITION p67 VALUES LESS THAN (to_days("2017-02-01")+2010),
  PARTITION p68 VALUES LESS THAN (to_days("2017-02-01")+2040),
  PARTITION p69 VALUES LESS THAN (to_days("2017-02-01")+2070),
  PARTITION p70 VALUES LESS THAN (to_days("2017-02-01")+2100),
  PARTITION p71 VALUES LESS THAN (to_days("2017-02-01")+2130),
  PARTITION p72 VALUES LESS THAN (to_days("2017-02-01")+2160),
  PARTITION p73 VALUES LESS THAN (to_days("2017-02-01")+2190),
  PARTITION p74 VALUES LESS THAN (to_days("2017-02-01")+2220),
  PARTITION p75 VALUES LESS THAN (to_days("2017-02-01")+2250),
  PARTITION p76 VALUES LESS THAN (to_days("2017-02-01")+2280),
  PARTITION p77 VALUES LESS THAN (to_days("2017-02-01")+2310),
  PARTITION p78 VALUES LESS THAN (to_days("2017-02-01")+2340),
  PARTITION p79 VALUES LESS THAN (to_days("2017-02-01")+2370),
  PARTITION p80 VALUES LESS THAN (to_days("2017-02-01")+2400),
  PARTITION p81 VALUES LESS THAN (to_days("2017-02-01")+2430),
  PARTITION p82 VALUES LESS THAN (to_days("2017-02-01")+2460),
  PARTITION p83 VALUES LESS THAN (to_days("2017-02-01")+2490),
  PARTITION p84 VALUES LESS THAN (to_days("2017-02-01")+2520),
  PARTITION p85 VALUES LESS THAN (to_days("2017-02-01")+2550),
  PARTITION p86 VALUES LESS THAN (to_days("2017-02-01")+2580),
  PARTITION p87 VALUES LESS THAN (to_days("2017-02-01")+2610),
  PARTITION p88 VALUES LESS THAN (to_days("2017-02-01")+2640),
  PARTITION p89 VALUES LESS THAN (to_days("2017-02-01")+2670),
  PARTITION p90 VALUES LESS THAN (to_days("2017-02-01")+2700),
  PARTITION p91 VALUES LESS THAN (to_days("2017-02-01")+2730),
  PARTITION p92 VALUES LESS THAN (to_days("2017-02-01")+2760),
  PARTITION p93 VALUES LESS THAN (to_days("2017-02-01")+2790),
  PARTITION p94 VALUES LESS THAN (to_days("2017-02-01")+2820),
  PARTITION p95 VALUES LESS THAN (to_days("2017-02-01")+2850),
  PARTITION p96 VALUES LESS THAN (to_days("2017-02-01")+2880),
  PARTITION p97 VALUES LESS THAN (to_days("2017-02-01")+2910),
  PARTITION p98 VALUES LESS THAN (to_days("2017-02-01")+2940),
  PARTITION p99 VALUES LESS THAN (to_days("2017-02-01")+2970),
  PARTITION p100 VALUES LESS THAN (to_days("2017-02-01")+3000),
  PARTITION p101 VALUES LESS THAN (to_days("2017-02-01")+3030),
  PARTITION p102 VALUES LESS THAN (to_days("2017-02-01")+3060),
  PARTITION p103 VALUES LESS THAN (to_days("2017-02-01")+3090),
  PARTITION p104 VALUES LESS THAN (to_days("2017-02-01")+3120),
  PARTITION p105 VALUES LESS THAN (to_days("2017-02-01")+3150),
  PARTITION p106 VALUES LESS THAN (to_days("2017-02-01")+3180),
  PARTITION p107 VALUES LESS THAN (to_days("2017-02-01")+3210),
  PARTITION p108 VALUES LESS THAN (to_days("2017-02-01")+3240),
  PARTITION p109 VALUES LESS THAN (to_days("2017-02-01")+3270),
  PARTITION p110 VALUES LESS THAN (to_days("2017-02-01")+3300),
  PARTITION p111 VALUES LESS THAN (to_days("2017-02-01")+3330),
  PARTITION p112 VALUES LESS THAN (to_days("2017-02-01")+3360),
  PARTITION p113 VALUES LESS THAN (to_days("2017-02-01")+3390),
  PARTITION p114 VALUES LESS THAN (to_days("2017-02-01")+3420),
  PARTITION p115 VALUES LESS THAN (to_days("2017-02-01")+3450),
  PARTITION p116 VALUES LESS THAN (to_days("2017-02-01")+3480),
  PARTITION p117 VALUES LESS THAN (to_days("2017-02-01")+3510),
  PARTITION p118 VALUES LESS THAN (to_days("2017-02-01")+3540),
  PARTITION p119 VALUES LESS THAN (to_days("2017-02-01")+3570),
  PARTITION p120 VALUES LESS THAN (to_days("2017-02-01")+3600),
  PARTITION p121 VALUES LESS THAN (to_days("2017-02-01")+3630),
  PARTITION p122 VALUES LESS THAN (to_days("2017-02-01")+3660),
  PARTITION p123 VALUES LESS THAN (to_days("2017-02-01")+3690),
  PARTITION p124 VALUES LESS THAN (to_days("2017-02-01")+3720),
  PARTITION p125 VALUES LESS THAN (to_days("2017-02-01")+3750),
  PARTITION p126 VALUES LESS THAN (to_days("2017-02-01")+3780),
  PARTITION p127 VALUES LESS THAN (to_days("2017-02-01")+3810),
  PARTITION p128 VALUES LESS THAN (to_days("2017-02-01")+3840),
  PARTITION p129 VALUES LESS THAN (to_days("2017-02-01")+3870),
  PARTITION p130 VALUES LESS THAN (to_days("2017-02-01")+3900),
  PARTITION p131 VALUES LESS THAN (to_days("2017-02-01")+3930),
  PARTITION p132 VALUES LESS THAN (to_days("2017-02-01")+3960),
  PARTITION p133 VALUES LESS THAN (to_days("2017-02-01")+3990),
  PARTITION p134 VALUES LESS THAN (to_days("2017-02-01")+4020),
  PARTITION p135 VALUES LESS THAN (to_days("2017-02-01")+4050),
  PARTITION p136 VALUES LESS THAN (to_days("2017-02-01")+4080),
  PARTITION p137 VALUES LESS THAN (to_days("2017-02-01")+4110),
  PARTITION p138 VALUES LESS THAN (to_days("2017-02-01")+4140),
  PARTITION p139 VALUES LESS THAN (to_days("2017-02-01")+4170),
  PARTITION p140 VALUES LESS THAN (to_days("2017-02-01")+4200),
  PARTITION p141 VALUES LESS THAN (to_days("2017-02-01")+4230),
  PARTITION p142 VALUES LESS THAN (to_days("2017-02-01")+4260),
  PARTITION p143 VALUES LESS THAN (to_days("2017-02-01")+4290),
  PARTITION p144 VALUES LESS THAN (to_days("2017-02-01")+4320),
  PARTITION p145 VALUES LESS THAN (to_days("2017-02-01")+4350),
  PARTITION p146 VALUES LESS THAN (to_days("2017-02-01")+4380),
  PARTITION p147 VALUES LESS THAN (to_days("2017-02-01")+4410),
  PARTITION p148 VALUES LESS THAN (to_days("2017-02-01")+4440),
  PARTITION p149 VALUES LESS THAN (to_days("2017-02-01")+4470),
  PARTITION p150 VALUES LESS THAN (to_days("2017-02-01")+4500),
  PARTITION p151 VALUES LESS THAN (to_days("2017-02-01")+4530),
  PARTITION p152 VALUES LESS THAN (to_days("2017-02-01")+4560),
  PARTITION p153 VALUES LESS THAN (to_days("2017-02-01")+4590),
  PARTITION p154 VALUES LESS THAN (to_days("2017-02-01")+4620),
  PARTITION p155 VALUES LESS THAN (to_days("2017-02-01")+4650),
  PARTITION p156 VALUES LESS THAN (to_days("2017-02-01")+4680),
  PARTITION p157 VALUES LESS THAN (to_days("2017-02-01")+4710),
  PARTITION p158 VALUES LESS THAN (to_days("2017-02-01")+4740),
  PARTITION p159 VALUES LESS THAN (to_days("2017-02-01")+4770),
  PARTITION p160 VALUES LESS THAN (to_days("2017-02-01")+4800),
  PARTITION p161 VALUES LESS THAN (to_days("2017-02-01")+4830),
  PARTITION p162 VALUES LESS THAN (to_days("2017-02-01")+4860),
  PARTITION p163 VALUES LESS THAN (to_days("2017-02-01")+4890),
  PARTITION p164 VALUES LESS THAN (to_days("2017-02-01")+4920),
  PARTITION p165 VALUES LESS THAN (to_days("2017-02-01")+4950),
  PARTITION p166 VALUES LESS THAN (to_days("2017-02-01")+4980),
  PARTITION p167 VALUES LESS THAN (to_days("2017-02-01")+5010),
  PARTITION p168 VALUES LESS THAN (to_days("2017-02-01")+5040),
  PARTITION p169 VALUES LESS THAN (to_days("2017-02-01")+5070),
  PARTITION p170 VALUES LESS THAN (to_days("2017-02-01")+5100),
  PARTITION p171 VALUES LESS THAN (to_days("2017-02-01")+5130),
  PARTITION p172 VALUES LESS THAN (to_days("2017-02-01")+5160),
  PARTITION p173 VALUES LESS THAN (to_days("2017-02-01")+5190),
  PARTITION p174 VALUES LESS THAN (to_days("2017-02-01")+5220),
  PARTITION p175 VALUES LESS THAN (to_days("2017-02-01")+5250),
  PARTITION p176 VALUES LESS THAN (to_days("2017-02-01")+5280),
  PARTITION p177 VALUES LESS THAN (to_days("2017-02-01")+5310),
  PARTITION p178 VALUES LESS THAN (to_days("2017-02-01")+5340),
  PARTITION p179 VALUES LESS THAN (to_days("2017-02-01")+5370),
  PARTITION p180 VALUES LESS THAN (to_days("2017-02-01")+5400),
  PARTITION p181 VALUES LESS THAN (to_days("2017-02-01")+5430),
  PARTITION p182 VALUES LESS THAN (to_days("2017-02-01")+5460),
  PARTITION p183 VALUES LESS THAN (to_days("2017-02-01")+5490),
  PARTITION p184 VALUES LESS THAN (to_days("2017-02-01")+5520),
  PARTITION p185 VALUES LESS THAN (to_days("2017-02-01")+5550),
  PARTITION p186 VALUES LESS THAN (to_days("2017-02-01")+5580),
  PARTITION p187 VALUES LESS THAN (to_days("2017-02-01")+5610),
  PARTITION p188 VALUES LESS THAN (to_days("2017-02-01")+5640),
  PARTITION p189 VALUES LESS THAN (to_days("2017-02-01")+5670),
  PARTITION p190 VALUES LESS THAN (to_days("2017-02-01")+5700),
  PARTITION p191 VALUES LESS THAN (to_days("2017-02-01")+5730),
  PARTITION p192 VALUES LESS THAN (to_days("2017-02-01")+5760),
  PARTITION p193 VALUES LESS THAN (to_days("2017-02-01")+5790),
  PARTITION p194 VALUES LESS THAN (to_days("2017-02-01")+5820),
  PARTITION p195 VALUES LESS THAN (to_days("2017-02-01")+5850),
  PARTITION p196 VALUES LESS THAN (to_days("2017-02-01")+5880),
  PARTITION p197 VALUES LESS THAN (to_days("2017-02-01")+5910),
  PARTITION p198 VALUES LESS THAN (to_days("2017-02-01")+5940),
  PARTITION p199 VALUES LESS THAN (to_days("2017-02-01")+5970),
  PARTITION p200 VALUES LESS THAN (to_days("2017-02-01")+6000)
);

/**
  玩家渠道映射表
 */
DROP TABLE IF EXISTS `uuser`;
CREATE TABLE `uuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appID` int(11) NOT NULL,
  `channelID` int(11) NOT NULL,
  `channelUserID` varchar(255) DEFAULT NULL,
  `channelUserName` varchar(255) DEFAULT NULL,
  `channelUserNick` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `extension` text,
  PRIMARY KEY (`id`),
  KEY `UCHANNEL_USER_ID` (`channelUserID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/**
  创建清理订单存储过程
  这个存储过程会删除uorder前天到昨天的订单数据 , 并将已完成(成功和失败的订单)的数据存入到uorder_bak表中
 */
DROP PROCEDURE IF EXISTS `proce_bak_orders`;
DELIMITER ;;
CREATE PROCEDURE `proce_bak_orders`()
BEGIN
	#Routine body goes here...
	/**删除过期的未完成的订单**/
	DECLARE beginTime DATETIME;
	DECLARE endTime DATETIME;
	

	SET beginTime = DATE_SUB(CURDATE(),INTERVAL 2 DAY);
	SET endTime = DATE_SUB(CURDATE(),INTERVAL 1 DAY);
	INSERT INTO `proce_log` (`message`, `type`, `eventTime`) VALUES (CONCAT("begin to clear uorder:",beginTime,"~",endTime), "BEGIN", NOW());

	INSERT INTO `uorder_bak` (`orderID`, `appID`, `channelID`, `channelOrderID`, `currency`, `extension`, `money`, `state`, `userID`, `roleID`, `roleName`, `serverID`, `serverName`, `createdTime`, `productName`, `productDesc`, `username`, `diamond`)
	(SELECT
			orderID,
			appID,
			channelID,
			channelOrderID,
			currency,
			extension,
			money,
			state,
			userID,
			roleID,
			roleName,
			serverID,
			serverName,
			createdTime,
			productName,
			productDesc,
			username,
			diamond
		FROM
			uorder
		WHERE
			state IN (2, 4)
		AND 
			createdTime BETWEEN beginTime
			AND endTime
		);
	INSERT INTO `proce_log` (`message`, `type`, `eventTime`) VALUES (CONCAT("copy uorder complet:",beginTime,"~",endTime), "END", NOW());
	DELETE FROM `uorder` WHERE createdTime BETWEEN beginTime AND endTime;
	INSERT INTO `proce_log` (`message`, `type`, `eventTime`) VALUES (CONCAT("delete uorder complet:",beginTime,"~",endTime), "END", NOW());
END
;;
DELIMITER ;

/**每晚4点进行一次订单清理*/
drop event if exists dayStatEventJob;

create event if not exists dayStatEventJob
ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(CURDATE(),INTERVAL 4 HOUR)  
on completion PRESERVE
do call proce_bak_orders();

ALTER EVENT dayStatEventJob ON  COMPLETION PRESERVE ENABLE;
DELIMITER ;


