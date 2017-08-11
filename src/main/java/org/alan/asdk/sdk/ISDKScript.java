package org.alan.asdk.sdk;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;

/**
 * SDK操作脚本
 * 第三方登录认证的校验
 * <p/>
 * 关于项目中编码问题
 * tomcat中sever.xml中Connector中需要设置编码为utf-8
 * web.xml中
 * struts.xml中
 * 都设置为utf-8
 */
public interface ISDKScript {

    void verify(UChannel channel, String extension, ISDKVerifyListener callback);

    void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback);

}
