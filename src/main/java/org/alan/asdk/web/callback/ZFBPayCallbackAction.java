package org.alan.asdk.web.callback;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.FriendListVO;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.cache.impl.logic.UChannelCache;
import org.alan.asdk.cache.impl.logic.UGameCache;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGame;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.zhifubao.ZFBSDK;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.utils.IDGenerator;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created on 2017/5/8.
 * 支付宝回调
 *
 * @author Chow
 * @since 1.0
 */
@Controller
@Scope("prototype")
@Namespace("/pay/zfb")
public class ZFBPayCallbackAction extends UActionSupport {

    /**
     * 获取好友时的openID
     */
    private String openID;
    /**
     * 是否展示自己
     */
    private int isShowSelf;

    /**
     * 1=获取双向好友
     * 2=获取双向+单向好友
     */
    private int getType;

    private String receiver_id;

    private String receiver_usertype;

    private String template_type;

    private String template_data;

    private String link;

    private String biz_memo;

    //支付回调参数在最后

    //现金红包参数
    private String coupon_name;
    private String prize_type;
    private String total_money;
    private String total_num;
    private String prize_msg;
    private String start_time;
    private String end_time;
    private String merchant_link;
    private String channelId;
    private String crowd_no;


    @Autowired
    private UUserManager userManager;

    @Autowired
    private UOrderManager orderManager;


    @Action("payCallback")
    public void payCallback() {
        try {
            //base64解码
            Log.i("收到<支付宝>回调:" + urlDecord() + ",\r\n sign = " + sign);
            //获取订单信息
            UOrder order = orderManager.getOrder(Long.parseLong(out_trade_no));
            //判断是否为重复订单
            if (order == null || order.getState() == PayState.STATE_SUC) {
                Log.i("<支付宝>订单[" + out_trade_no + "]重复 或不存在:" + urlDecord());
                return;
            }
            //验签
            UChannel channel = order.getChannel();
            Log.i("支付宝开始验签,urlDecord() =" + urlDecord());
            boolean signVerified = AlipaySignature.rsaCheckV1(urlDecord(), channel.getCpPayKey(), "UTF-8", "RSA2");
            if (!signVerified) {
                Log.i("<支付宝>回调验签失败:" + urlDecord());
                renderText("failure");
                return;
            }
            //判断金额
            int amount = Integer.parseInt(total_amount);
            if (amount != order.getMoney()) {
                Log.i("<支付宝>订单金额不等于实际金额,按照实际金额[" + order.getMoney() + "]发奖:" + urlDecord());
                order.setProductName("com.muzhiyouwan.bzddt_any");
                order.setMoney(amount);
            }
            //判断appId是否一致
            if (!app_id.equals(channel.getCpAppID())) {
                Log.i("<支付宝>appId不一致 , 配置的appID 是" + channel.getCpAppID() + ":" + urlDecord());
                renderText("failure");
                return;
            }
            //通知发奖
            order.setChannelOrderID(trade_no);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            Log.i("<支付宝>订单充值成功:" + urlDecord());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("<支付宝>充值订单失败 , e =" + e.getMessage());
            renderText("failure");
        }
    }

    @Action("getZFBFriends")
    public void getZFBFriends() {
        try {
            long ms = System.currentTimeMillis();
            Log.i("开始获取<支付宝>好友: " + this + ",0代表不展示自己 , 其他代表展示自己");
            UUser user = userManager.getUser(Integer.parseInt(openID));
            if (user == null) {
                Log.i("<支付宝>获取好友失败 : " + this + ",查无此人");
                renderState(StateCode.CODE_USER_NONE, JSONObject.fromObject("{msg:\"用户不存在\"}"));
                return;
            }
            AlipayClient client = ZFBSDK.getClient(new ZFBSDK.Params(user.getChannel()));
            AlipaySocialBaseRelationFriendsQueryRequest request = new AlipaySocialBaseRelationFriendsQueryRequest();
            request.setBizContent("{" +
                    " \"get_type\":" + getType + "," +
                    " \"include_self\":" + (isShowSelf != 0) + "" +
                    " }");
            AlipaySocialBaseRelationFriendsQueryResponse response = client.execute(request, user.getChannelUserName());
            if (response.isSuccess()) {
                List<FriendListVO> friendList = response.getFriendList();
                Log.i("<支付宝>获取好友成功 开始处理 :" + this);
                JSONArray jsonArray = new JSONArray();
                for (FriendListVO friendListVO : friendList) {
                    String userId = friendListVO.getUserId();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("head_img", friendListVO.getHeadImg());
                    jsonObject.put("real_friend", friendListVO.getRealFriend());
                    jsonObject.put("user_id", friendListVO.getUserId());
                    jsonObject.put("view_name", friendListVO.getViewName());
                    UUser friend = userManager.getUserByCpID(user.getAppID(), user.getChannelID(), userId);
                    if (friend == null) {
                        Log.i("<支付宝>获取好友,未在用户内找到该支付宝用户 : " + this + ",支付宝userId = " + userId);
                        jsonObject.put("openID", "-1");
                    } else {
                        jsonObject.put("openID", friend.getId());
                    }
                    jsonArray.add(jsonObject);
                }
                renderState(StateCode.CODE_AUTH_SUCCESS, jsonArray);
                Log.i("<支付宝>获取好友发送成功 : " + this + ", friendList = " + jsonArray.toString() + ", 耗时 " + (System.currentTimeMillis() - ms) + "ms");
            } else {
                Log.i("<支付宝>获取好友失败 : " + this + " , response = " + response.getBody() + ", 耗时 " + (System.currentTimeMillis() - ms) + "ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"其他错误\"}"));
        }
    }

    @Action("getZFBUserInfo")
    public void getZFBUserInfo() {
        try {
            long ms = System.currentTimeMillis();
            Log.i("开始获取<支付宝>用户信息: " + this);
            UUser user = userManager.getUser(Integer.parseInt(openID));
            if (user == null) {
                Log.i("<支付宝>获取自身信息失败 : " + this + ",查无此人");
                renderState(StateCode.CODE_USER_NONE, JSONObject.fromObject("{msg:\"用户不存在\"}"));
                return;
            }
            AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
            AlipayClient client = ZFBSDK.getClient(new ZFBSDK.Params(user.getChannel()));
            AlipayUserInfoShareResponse userinfoShareResponse = client.execute(request, user.getChannelUserName());
            if (userinfoShareResponse.isSuccess()) {
                Log.i("获取<支付宝>用户信息成功: " + this + " , 开始解析 :" + userinfoShareResponse.getBody());
                renderState(StateCode.CODE_AUTH_SUCCESS, JSONObject.fromObject(userinfoShareResponse.getBody()));
                Log.i("获取<支付宝>用户信息发送成功: " + this + ", 耗时 " + (System.currentTimeMillis() - ms) + "ms");
            } else {
                Log.i("获取<支付宝>用户信息失败: " + this + ", body = " + userinfoShareResponse.getBody() + ", 耗时 " + (System.currentTimeMillis() - ms) + "ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝静默分享
     */
    @Action("ZFBShareChart")
    public void shareChat() {
        try {
            long ms = System.currentTimeMillis();
            Log.i("<支付宝>开始静默分享 : " + this);
            UUser user = userManager.getUser(Integer.parseInt(openID));
            if (user == null) {
                Log.i("<支付宝>静默分享失败 : " + this + ",查无此人");
                renderState(StateCode.CODE_USER_NONE, JSONObject.fromObject("{msg:\"用户不存在\"}"));
                return;
            }
            AlipayClient client = ZFBSDK.getClient(new ZFBSDK.Params(user.getChannel()));
            AlipaySocialBaseChatSendRequest request = new AlipaySocialBaseChatSendRequest();
            long id = IDGenerator.getInstance().nextOrderID();
            JSONObject bizContent = new JSONObject();
            bizContent.put("client_msg_id", id);
            bizContent.put("receiver_id", receiver_id);
            bizContent.put("receiver_usertype", receiver_usertype);
            bizContent.put("template_type", template_type);
            bizContent.put("template_data", template_data);
            bizContent.put("link", link);
            bizContent.put("biz_memo", biz_memo);
            request.setBizContent(bizContent.toString());
            AlipaySocialBaseChatSendResponse response = client.execute(request, user.getChannelUserName());
            if (response.isSuccess()) {
                renderState(StateCode.CODE_AUTH_SUCCESS, JSONObject.fromObject(response.getBody()));
                Log.i("<支付宝>静默分享成功! params = " + this.toString() + ", body = " + response.getBody() + ", 耗时:" + (System.currentTimeMillis() - ms) + "ms");
            } else {
                Log.i("<支付宝>静默分享失败! params = " + this.toString() + ", body = " + response.getBody() + ", 耗时:" + (System.currentTimeMillis() - ms) + "ms");
                renderState(StateCode.CODE_AUTH_FAILED, response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderState(StateCode.CODE_AUTH_FAILED, "系统错误 e=" + e.getMessage());
        }

    }

    /**
     * 创建现金红包
     */
    @Action("createCash")
    public void createCash() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("coupon_name", coupon_name);
            params.put("prize_type", prize_type);
            params.put("total_money", total_money);
            params.put("total_num", total_num);
            params.put("prize_msg", prize_msg);
            params.put("start_time", start_time);
            params.put("end_time", end_time);
            params.put("merchant_link", merchant_link);
            params.put("channelId", channelId);
            String content = AlipaySignature.getSignCheckContentV1(params);
            Log.i("收到<支付宝>创建现金红包 , params = " + content);
            UChannel channel = UChannelCache.getInstance().get(Integer.valueOf(channelId));
            if (channel == null) {
                Log.i("<支付宝>创建现金红包出错 , channel无法查找 =" + this.channelId);
                renderState(StateCode.CODE_CHANNEL_NONE, JSONObject.fromObject("{msg:\"未知错误\"}"));
                return;
            }
            if (!validParams(content, channel)) {
                Log.i("<支付宝>创建现金红包出错 , 验签失败");
                renderState(StateCode.CODE_SIGN_ERROR, JSONObject.fromObject("{msg:\"验签失败\"}"));
                return;
            }
            //全部成功开始创建
            AlipayClient alipayClient = ZFBSDK.getClient(new ZFBSDK.Params(channel));
            AlipayMarketingCampaignCashCreateRequest request = new
                    AlipayMarketingCampaignCashCreateRequest();
            request.setBizContent(JSONObject.fromObject(params).toString());
            AlipayMarketingCampaignCashCreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                renderState(StateCode.CODE_AUTH_SUCCESS, JSONObject.fromObject(response.getBody()));
                Log.i("<支付宝>创建现金红包成功 , body = " + response.getBody());
            } else {
                renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"支付宝返回错误\"}"));
                Log.i("<支付宝>创建现金红包失败, 支付宝返回错误 , body = " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"系统出错\"}"));
        }
    }

    @Action("triggerCash")
    public void triggerCash() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("crowd_no", this.crowd_no);
            map.put("openID", this.openID);
            String content = AlipaySignature.getSignCheckContentV1(map);
            UUser user = userManager.getUser(Integer.parseInt(openID));
            Log.i("收到<支付宝>触发现金红包 , params = " + map);
            if (user == null) {
                Log.i("<支付宝>触发现金红包出错 : " + this + ",查无此人");
                renderState(StateCode.CODE_USER_NONE, JSONObject.fromObject("{msg:\"用户不存在\"}"));
                return;
            }
            if (!validParams(content, user.getChannel())) {
                Log.i("<支付宝>触发现金红包出错 , 验签失败");
                renderState(StateCode.CODE_SIGN_ERROR, JSONObject.fromObject("{msg:\"验签失败\"}"));
                return;
            }
            //开始创建
            AlipayClient alipayClient = ZFBSDK.getClient(new ZFBSDK.Params(user.getChannel()));
            AlipayMarketingCampaignCashTriggerRequest request = new AlipayMarketingCampaignCashTriggerRequest();
            map.remove("openID");
            map.put("user_id",user.getChannelUserID());
            request.setBizContent(JSONObject.fromObject(map).toString());
            AlipayMarketingCampaignCashTriggerResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                renderState(StateCode.CODE_AUTH_SUCCESS, JSONObject.fromObject(response.getBody()));
                Log.i("<支付宝>触发现金红包成功 , body = " + response.getBody());
            } else {
                renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"支付宝返回错误\"}"));
                Log.i("<支付宝>触发现金红包失败, 支付宝返回错误 , body = " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"系统出错\"}"));
        }
    }

    /**
     * 验证参数
     *
     * @return
     */
    private boolean validParams(String content, UChannel channel) {
        UGame game = UGameCache.getInstance().get(channel.getAppID());
        if (game == null) {
            Log.i("<支付宝>验证参数出错 , game = null");
            return false;
        }
        String s = content + game.getAppkey();
        String md5 = EncryptUtils.md5(s);
        if (Objects.equals(s, md5)) {
            Log.i("<支付宝>验证参数出错 , 待验签字符=" + s + ",本地结果=" + md5 + ",远程结果=" + this.sign);
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ZFBPayCallbackAction{" +
                "openID='" + openID + '\'' +
                ", isShowSelf=" + isShowSelf +
                ", getType=" + getType +
                ", receiver_id='" + receiver_id + '\'' +
                ", receiver_usertype='" + receiver_usertype + '\'' +
                ", template_type='" + template_type + '\'' +
                ", template_data='" + template_data + '\'' +
                ", link='" + link + '\'' +
                ", biz_memo='" + biz_memo + '\'' +
                '}';
    }

    private void renderState(int state, JSON data) {
        try {
            JSONObject json = new JSONObject();
            json.put("state", state);
            json.put("data", data);
            renderJson(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }
    }

    private void renderState(int state, String data) {
        try {
            JSONObject json = new JSONObject();
            json.put("state", state);
            json.put("data", data);
            renderJson(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }
    }


    private Map<String, String> urlDecord() {
        Map requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }


    private String notify_time;
    private String notify_type;
    private String notify_id;
    private String app_id;
    private String charset;
    private String version;
    private String sign_type;
    private String sign;
    private String trade_no;
    private String out_trade_no;
    private String out_biz_no;
    private String buyer_id;
    private String buyer_logon_id;
    private String seller_id;
    private String seller_email;
    private String trade_status;
    private String total_amount;
    private String receipt_amount;
    private String invoice_amount;
    private String buyer_pay_amount;
    private String point_amount;
    private String refund_fee;
    private String subject;
    private String body;
    private String gmt_create;
    private String gmt_payment;
    private String gmt_refund;
    private String gmt_close;
    private String fund_bill_list;
    private String passback_params;
    private String voucher_detail_list;

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public String getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(String buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public String getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(String point_amount) {
        this.point_amount = point_amount;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(String gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    public String getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(String gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getFund_bill_list() {
        return fund_bill_list;
    }

    public void setFund_bill_list(String fund_bill_list) {
        this.fund_bill_list = fund_bill_list;
    }

    public String getPassback_params() {
        return passback_params;
    }

    public void setPassback_params(String passback_params) {
        this.passback_params = passback_params;
    }

    public String getVoucher_detail_list() {
        return voucher_detail_list;
    }

    public void setVoucher_detail_list(String voucher_detail_list) {
        this.voucher_detail_list = voucher_detail_list;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public int getIsShowSelf() {
        return isShowSelf;
    }

    public void setIsShowSelf(int isShowSelf) {
        this.isShowSelf = isShowSelf;
    }

    public int getGetType() {
        return getType;
    }

    public void setGetType(int getType) {
        this.getType = getType;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getReceiver_usertype() {
        return receiver_usertype;
    }

    public void setReceiver_usertype(String receiver_usertype) {
        this.receiver_usertype = receiver_usertype;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getTemplate_data() {
        return template_data;
    }

    public void setTemplate_data(String template_data) {
        this.template_data = template_data;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBiz_memo() {
        return biz_memo;
    }

    public void setBiz_memo(String biz_memo) {
        this.biz_memo = biz_memo;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getPrize_type() {
        return prize_type;
    }

    public void setPrize_type(String prize_type) {
        this.prize_type = prize_type;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getPrize_msg() {
        return prize_msg;
    }

    public void setPrize_msg(String prize_msg) {
        this.prize_msg = prize_msg;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getMerchant_link() {
        return merchant_link;
    }

    public void setMerchant_link(String merchant_link) {
        this.merchant_link = merchant_link;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCrowd_no() {
        return crowd_no;
    }

    public void setCrowd_no(String crowd_no) {
        this.crowd_no = crowd_no;
    }
}
