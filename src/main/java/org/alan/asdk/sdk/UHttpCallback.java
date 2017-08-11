package org.alan.asdk.sdk;

/**
 *
 */
public interface UHttpCallback {

    void completed(String content);

    void failed(String err);

}
