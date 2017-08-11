package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGoods;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.THttpAgent;
import org.alan.asdk.service.UGoodsManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 苹果充值客户端回调
 *
 * @author Lance Chow
 * @create 2016-06-02 11:14
 */
@Controller
@Scope("prototype")
@Namespace("/pay/apple")
public class AppleCallbackAction extends UActionSupport {


    private String orderID;

    private String verifyInfo;

    private static final String VERFIY_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private static final String VERFIY_URL_SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";

    @Autowired
    private UGoodsManager goodsManager;
    @Autowired
    private UOrderManager orderManager;

    @Action("verify")
    public void payCallback() {
        Log.i("苹果充值开始,orderID = " + orderID + ",verifyInfo =" + verifyInfo);
        //转义
        this.verifyInfo = this.verifyInfo.replaceAll(" ","+");
        try {
            if (StringUtils.isEmpty(verifyInfo)) {
                Log.i("<Apple> order [" + orderID + "] , verifyInfo isEmpty.");
                renderState(false, "verifyInfo isEmpty.");
                return;
            }

//            String originalTransactionId = getOriginalTransactionId(verifyInfo);
//
//            if (StringUtils.isEmpty(originalTransactionId)) {
//                Log.i("<Apple> order [" + orderID + "] , originalTransactionId isEmpty.");
//                renderState(false, "originalTransactionId isEmpty.");
//                return;
//            }

            //获取订单
            long orderID = Long.parseLong(this.orderID);

            UOrder order = orderManager.getOrder(orderID);
            if (order == null) {
                Log.i("<Apple> order [" + this.orderID + "] , The order is null!");
                renderState(false, "order is invaild.");
                return;
            }

            //验证订单是否完成
            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<Apple> order [" + orderID + "] The pay is complete , the order is repeat");
                renderState(false, "order is invaild.");
                return;
            }
            String res = validToApple(VERFIY_URL);
            Log.i("苹果返回值:"+res);
            if (res == null) {
                renderState(false, "order is invaild.");
                return;
            }
            UChannel channel = order.getChannel();
            if (channel == null) {
                Log.i("<Apple> order [" + orderID + "] the channel is null!");
                renderState(false, "channel is null.");
                return;
            }
            JSONObject resJSON = JSONObject.fromObject(res);
            if (resJSON.getInt("status")!= 0){
                Log.i("苹果订单["+orderID+"] status 错误 status = "+ resJSON.getInt("status"));
                renderState(false, "status is error.");
                return;
            }
            resJSON = resJSON.getJSONObject("receipt");
            String productId = resJSON.getJSONArray("in_app").getJSONObject(0).getString("product_id");
            if (!order.getProductName().equals(productId)){
                Log.i("苹果订单["+orderID+"] product_id 错误 productId = "+ productId+",订单本身是:"+order.getProductName()+",重新设置productId");
                order.setProductName(productId);
            }
            //通过苹果的验证获取商品
            UGoods goods = goodsManager.getGoodsBySdkGoodsID(channel.getGoodsConfigID(), order.getProductName());
            if (goods == null) {
                Log.i("<Apple> order [" + orderID + "] goods is empty");
                renderState(false, "product_id is invaild.");
                return;
            }
            if (goods.getMoney() != order.getMoney()) {
                Log.i("<Apple> order [" + orderID + "] The money is not consistent , apple order money[" + goods.getMoney() + "] , order money : " + order.getMoney() + "");
                order.setMoney(goods.getMoney());
                order.setDiamond(goods.getDiamond());
            }
            //验证时间是否过期
            long appleChargeTime = Long.parseLong(resJSON.getString("original_purchase_date_ms"));//苹果传来的时间
            long createOrderTime = order.getCreatedTime().getTime();

            if ((appleChargeTime - createOrderTime) >= 24 * 60 * 60 * 1000) {
                Log.i("<Apple> order [" + orderID + "] abnormal time is more than 24 hours , appleChargeTime=" + appleChargeTime + ", createOrderTime=" + createOrderTime);
                renderState(false, "order is invaild.");
                return;
            }

            String original_transaction_id = resJSON.getJSONArray("in_app").getJSONObject(0).getString("original_transaction_id");
            order.setChannelOrderID(original_transaction_id);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            renderState(true, "success");
            Log.i("<Apple> order [" + orderID + "] pay success!");
        } catch (Exception e) {
            Log.e("<Apple> order [" + this.orderID + "] is Error", e);
            renderState(false, "order is invalid");
            e.printStackTrace();
        }
    }

    /**
     * 向苹果服务器做验证
     *
     * @return result 苹果的验证消息
     */
    private String validToApple(String url) {
        //到苹果服务器上进行验证
        JSONObject params = new JSONObject();
        params.put("receipt-data", this.verifyInfo);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String content = null;
        for (int i = 0; i < 3; i++) {
            content = THttpAgent.getInstance().post(url, headers, new ByteArrayEntity(params.toString().getBytes(Charset.forName("UTF-8"))));
            if (content != null) {
                break;
            }
        }
        if (content == null) {
            return null;
        }
        JSONObject object = JSONObject.fromObject(content);
        int status = object.getInt("status");
        if (status == 0) {
            return content;
        } else if (status == 21008) {
            return validToApple(VERFIY_URL);
        } else if (status == 21007) {
            return validToApple(VERFIY_URL_SANDBOX);
        }
        Log.i("<Apple> order [" + orderID + "] verify failed , apple return:" + content);
        return null;
    }

//    public static void main(String[] args) {
//        Map<String, String> params = new HashMap<>();
//        params.put("verifyInfo", "ewoJInNpZ25hdHVyZSIgPSAiQTNsK05KNTVCS2U2eFMyN1haZjRRRzdGMHlibHhvWWZKa0kyWlpZWG5LemI5YUJlRXhLVkFGdnFvckdacDV6MGw2K2pOZmhrSi8xbmlOYnYvSlI1NVdlZFhQNnNFN21QWHZ2TU9oVXVYUmFzTWZBeXZFSERzQUZIdFVsTldjMDVNdXVIelVDbW9Daks0SFM5QU5yTU1ZM1RwQkFyQXhxeWl0aDFIcHgyMHdEaGVSNWVWMHdCczNUUXVXUEtGaGFhTE9jd2ppamhBT0hsN1FKYVBMaXFEVC9BY2R4WWlNYzJsVlFDZjF1bFJPRFlKZ0xmNWV6SHpycDdES2kxUGNiaS9NNjZ1ZFh5dTIxbUZqWW1xRUNPUDJZMVhJQWVINDZYTDdIdXZXd256NHBBOHVDaWRDWnN3SXhHNW51VnhpYVpwS1oxMlMxNko4VXMrMVJxb3NOSGV4a0FBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEEzTFRFeElEQXdPalV4T2pBMklFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5WdWFYRjFaUzFwWkdWdWRHbG1hV1Z5SWlBOUlDSTFOR1F3TURFNE1tVTJNV0ZrTXpRd01tVmtOV1kwTTJNME9UWmtPRFl4TW1FMU16STRabVV6SWpzS0NTSnZjbWxuYVc1aGJDMTBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNVEF3TURBd01ESXlNamd6TkRBMU15STdDZ2tpWW5aeWN5SWdQU0FpTVM0eklqc0tDU0owY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREl5TWpnek5EQTFNeUk3Q2draWNYVmhiblJwZEhraUlEMGdJakVpT3dvSkltOXlhV2RwYm1Gc0xYQjFjbU5vWVhObExXUmhkR1V0YlhNaUlEMGdJakUwTmpneU1qTTBOall3TURBaU93b0pJblZ1YVhGMVpTMTJaVzVrYjNJdGFXUmxiblJwWm1sbGNpSWdQU0FpUkRZNE1rVkNNelV0T0VZd1F5MDBNa1ZDTFRrd1EwUXROekF6TlRnMk5VRkROMEpCSWpzS0NTSndjbTlrZFdOMExXbGtJaUE5SUNKamIyMHVkSE5wZUdrdWMyOTFiR1ZrWjJVdWRHZ3VNeUk3Q2draWFYUmxiUzFwWkNJZ1BTQWlNVEV5TkRJNE1UQTBNU0k3Q2draVltbGtJaUE5SUNKamIyMHVkSE5wZUdrdWMyOTFiR1ZrWjJVdWRHZ2lPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTBOamd5TWpNME5qWXdNREFpT3dvSkluQjFjbU5vWVhObExXUmhkR1VpSUQwZ0lqSXdNVFl0TURjdE1URWdNRGM2TlRFNk1EWWdSWFJqTDBkTlZDSTdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTMXdjM1FpSUQwZ0lqSXdNVFl0TURjdE1URWdNREE2TlRFNk1EWWdRVzFsY21sallTOU1iM05mUVc1blpXeGxjeUk3Q2draWIzSnBaMmx1WVd3dGNIVnlZMmhoYzJVdFpHRjBaU0lnUFNBaU1qQXhOaTB3TnkweE1TQXdOem8xTVRvd05pQkZkR012UjAxVUlqc0tmUT09IjsKCSJlbnZpcm9ubWVudCIgPSAiU2FuZGJveCI7CgkicG9kIiA9ICIxMDAiOwoJInNpZ25pbmctc3RhdHVzIiA9ICIwIjsKfQ==");
//        params.put("orderID", "997131946880401433");
//        String content = THttpAgent.getInstance().post("http://tsdk.asia.tsixi.com:8080/TSDK/pay/apple/verify", params);
//        System.out.println(content);
//    }

    /**
     * 获取apple充值交易ID
     *
     * @param verifyInfoBase64 苹果返回的原始信息
     * @return OriginalTransactionId
     */
    private static String getOriginalTransactionId(String verifyInfoBase64) {

        if (verifyInfoBase64 == null || "".equals(verifyInfoBase64.trim())) return null;

        String purchaseInfoBase64Str = null;
        String original_transaction_id = null;

        String verifyInfo = new String(Base64.decodeBase64(verifyInfoBase64));
        Log.i("verifyInfo =" +verifyInfo);
        String[] verifyInfoOcStr = verifyInfo.split("\n");

        for (String p : verifyInfoOcStr) {
            if (p.contains("\"purchase-info\" = ")) {
                purchaseInfoBase64Str = p.substring(20, p.length() - 2);
                break;
            }
        }

        if (purchaseInfoBase64Str != null) {
            String purchaseInfo = new String(Base64.decodeBase64(purchaseInfoBase64Str));
            String[] purchaseInfoOcStr = purchaseInfo.split("\n");
            for (String pcOcStr : purchaseInfoOcStr) {
                if (pcOcStr.contains("\"original-transaction-id\" = ")) {
                    original_transaction_id = pcOcStr.substring(30, pcOcStr.length() - 2);
                    break;
                }
            }
        }

        return original_transaction_id;
    }

    public void renderState(boolean success, String resultMsg) {
        try {
            JSONObject json = new JSONObject();
            json.put("success", success);
            json.put("msg", resultMsg);
            renderJson(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getVerifyInfo() {
        return verifyInfo;
    }

    public void setVerifyInfo(String verifyInfo) {
        this.verifyInfo = verifyInfo;
    }
}
