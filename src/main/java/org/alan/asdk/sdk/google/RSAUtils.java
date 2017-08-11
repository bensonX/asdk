package org.alan.asdk.sdk.google;


import org.alan.asdk.sdk.appchina.Base64;
import org.alan.asdk.sdk.appchina.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;


/**
 * 谷歌RSA工具
 *
 * @author Lance Chow
 * @create 2016-07-04 18:28
 */
public class RSAUtils {

    public static Logger log = LoggerFactory.getLogger(RSAUtil.class);

    /**
     * Verifies that the data was signed with the given signature, and returns
     * the verified purchase. The data is in JSON format and signed
     * and product ID of the purchase.
     *
     * @param base64PublicKey the base64-encoded public key to use for verifying.
     * @param signedData      the signed JSON string (signed, not encrypted)
     * @param signature       the signature for the data, signed with the private key
     */
    public static boolean verifyPurchase(String base64PublicKey, String signedData, String signature) {

        if (signedData == null || "".equals(signedData.trim())
                || base64PublicKey == null || "".equals(base64PublicKey.trim())
                || signature == null || "".equals(signature.trim())
                ) {
            return false;
        }

        PublicKey key = generatePublicKey(base64PublicKey);
        return verify(key, signedData, signature);
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) {

        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (Exception e) {
            log.warn("generatePublicKey exception", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that the signature from the com.u8.server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey  public key associated with the developer account
     * @param signedData signed data from com.u8.server
     * @param signature  com.u8.server signature
     * @return true if the data and signature match
     */
    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        Signature sig;
        try {
            sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (!sig.verify(Base64.decode(signature.getBytes()))) {
                log.warn("Signature verification failed");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("Signature verification Exception", e);
        }
        return false;
    }

}
