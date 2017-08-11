package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.txmsdk.PayRequest;
import org.alan.asdk.sdk.txmsdk.TXMSDKManager;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UUserManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;


/**
 * 腾讯应用宝支付相关接口
 * <p/>
 * 腾讯应用宝不存在支付通知回调
 * <p/>
 * 客户端支付成功->发送查询余额请求->服务器去应用宝查询余额->查询成功，立马全部扣除->通知游戏服务器充值成功
 * <p/>
 * 为了防止丢单，客户端每次登录的时候，都向u8server发送一个余额查询请求
 * <p/>
 * 同时，收到客户端的余额查询之后，立马查询一次，查询到余额，则结束。如果没有查询到余额，可能是支付有延迟
 * 会生成一个延迟任务，放到延迟队列中，由另一个线程完成后续查询，持续2分钟，每20秒查询一次
 * <p/>
 * Created by ant on 2015/10/14.
 */

@Controller
@Scope("prototype")
@Namespace("/pay/txmsdk")
public class TXMSDKPayAction extends UActionSupport {

    private int opType;      //操作类型，1:正常查询;2:登录查询掉单
    private int channelID;      //当前渠道ID
    private int userID;         //当前用户ID
    private int accountType;    //0:QQ;1:微信
    private String openID;      //从手Q登录态或微信登录态中获取的openid的值
    private String openKey;     //从手Q登录态或微信登录态中获取的access_token 的值
    private String pay_token;   //从手Q登录态中获取的pay_token的值; 微信登录时特别注意该参数传空。
    private String pf;          //平台来源
    private String pfkey;       //跟平台来源和openkey根据规则生成的一个密钥串
    private String zoneid;      //账户分区ID

    @Autowired
    private UChannelManager channelManager;

    @Autowired
    private UUserManager userManager;

    @Action("charge")
    public void charge() {

        try {
            Log.i("<腾讯应用宝>充值回调开始： opType=" +opType+ ", channelID=" +channelID+ ", userID=" +userID+ ", accountType=" +accountType+ ", openID=" +openID+ ", openKey=" +openKey+ ", pay_token=" +pay_token+ ", pf=" +pf+ ", pfkey=" +pfkey+ ", zoneid=" +zoneid);


            UChannel channel = channelManager.queryChannel(this.channelID);
            if (channel == null) {
                Log.d("<腾讯应用宝>渠道不存在. channelID:%s", this.channelID);
                this.renderState(false);
                return;
            }

            UUser user = userManager.getUser(this.userID);
            if (user == null) {
                Log.e("<腾讯应用宝>用户不存在. userID:%s ", this.userID);
                this.renderState(false);
                return;
            }

            if (!user.getChannelUserID().equals(this.getOpenID())) {
                Log.e("<腾讯应用宝>the userID %s is not matched the channel userID %s. ", this.userID, this.openID);
                this.renderState(false);
                return;
            }

            PayRequest req = new PayRequest();
            req.setUser(user);
            req.setAccountType(accountType);
            req.setOpenID(openID);
            req.setOpenKey(openKey);
            req.setPay_token(pay_token);
            req.setPf(pf);
            req.setPfkey(pfkey);
            req.setZoneid(zoneid);

            TXMSDKManager.getInstance().addPayRequest(req);

            this.renderState(true);

        } catch (Exception e) {
            try {
                renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    private void chargeForLogin() {

        try {

            UUser user = userManager.getUser(this.userID);
            if (user == null) {
                renderState(false);
                Log.e("<腾讯应用宝>用户不存在. userID: %s ", this.userID);
                return;
            }


        } catch (Exception e) {
            try {
                renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("1");
        } else {
            this.response.getWriter().write("0");
        }


    }


    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }
}
