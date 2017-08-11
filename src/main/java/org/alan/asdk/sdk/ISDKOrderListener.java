package org.alan.asdk.sdk;

import java.util.EventListener;

/**
 * Created by ant on 2015/3/26.
 */
public interface ISDKOrderListener extends EventListener {

    void onSuccess(String jsonStr);

    void onFailed(String err);

}
