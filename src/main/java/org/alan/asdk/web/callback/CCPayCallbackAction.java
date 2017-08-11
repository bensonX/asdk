package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 虫虫游戏支付回调
 *
 * @author Lance Chow
 * @create 2016-04-25 15:18
 */
@Controller
@Scope("prototype")
@Namespace("/pay/cc")
public class CCPayCallbackAction extends UActionSupport {

    private String transactionNo;
    private String partnerTransactionNo;
    private String statusCode;
    private String productId;
    private String orderPrice;
    private String packageId;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
            Log.i("<虫虫>充值回调开始： transactionNo=" +transactionNo+ ", partnerTransactionNo=" +partnerTransactionNo+ ", statusCode=" +statusCode+ ", productId=" +productId+ ", orderPrice=" +orderPrice+ ", packageId=" +packageId+ ", sign=" +sign);
        System.out.println(request.getParameterMap());

        if (!"0000".equals(statusCode)){
            Log.i("<虫虫>["+transactionNo+"]返回状态不为0000,statusCode="+statusCode);
            renderText("success");
            return;
        }
        long orderID = Long.parseLong(partnerTransactionNo);
        UOrder order = orderManager.getOrder(orderID);
        if (order==null || order.getChannel() == null){
            Log.i("<虫虫>["+orderID+"]无法找到渠道或者订单对象");
            renderText("success");
            return;
        }
        if (order.getState() == PayState.STATE_SUC) {
            Log.i("<虫虫> 订单[" + orderID+"] 重复");
            renderText("success");
            return;
        }
        //验证签名
        if(!isValid(order)){
            renderText("success");
            return;
        }
        int d = (int)(Double.parseDouble(orderPrice)*100);
        if (d != order.getMoney() || d == 0){
            Log.i("<虫虫>订单["+orderID+"]金额orderPrice=["+orderPrice+"]不一致,money="+ order.getMoney());
            renderText("success");
            return;
        }
        //通知游戏服务器
        order.setChannelOrderID(transactionNo);
        SendAgent.sendCallbackToServer(orderManager,order);
        Log.i("<虫虫>订单["+orderID+"]充值成功!");
        renderText("success");
    }

    private boolean isValid(UOrder order) {
        String e =
                "orderPrice="+orderPrice+
                "&packageId="+packageId+
                "&partnerTransactionNo="+partnerTransactionNo+
                "&productId="+productId+
                "&statusCode="+statusCode+
                "&transactionNo="+transactionNo+"&"+order.getChannel().getCpAppKey();
        e = EncryptUtils.md5(e);
        if (!e.equals(sign)){
            Log.i("<虫虫>订单["+order.getOrderID()+"]签名sign["+sign+"]不一致,e="+e);
            return false;
        }
        return true;
    }

    /**
     * getter setter
     */

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getPartnerTransactionNo() {
        return partnerTransactionNo;
    }

    public void setPartnerTransactionNo(String partnerTransactionNo) {
        this.partnerTransactionNo = partnerTransactionNo;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
