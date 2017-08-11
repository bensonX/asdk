package org.alan.asdk.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAHelper {

	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = (new BASE64Decoder()).decodeBuffer(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 得到密钥字符串（经过base64编码）
	 * 
	 * @return
	 */
	public static String getKeyString(Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = (new BASE64Encoder()).encode(keyBytes);
		return s;
	}

	/**
	 * 生成公钥和私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> generateKeys() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		PublicKey publicKey = keyPair.getPublic();

		// 私钥
		PrivateKey privateKey = keyPair.getPrivate();

		Map<String, String> keyMap = new HashMap<String, String>(2);

		keyMap.put(PUBLIC_KEY, getKeyString(publicKey));
		keyMap.put(PRIVATE_KEY, getKeyString(privateKey));
		return keyMap;
	}

	public static String decode(byte[] code, String privateKey, String charSet)
			throws Exception {
		// 加解密类
		Cipher cipher = Cipher.getInstance("RSA");// Cipher.getInstance("RSA/ECB/PKCS1Padding");

		PrivateKey priKey = getPrivateKey(privateKey);

		// 解密
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		byte[] deBytes = cipher.doFinal(code);
		return new String(deBytes, charSet);
	}

	public static byte[] encode(String code, String publicKey, String charSet)
			throws Exception {
		// 加解密类
		Cipher cipher = Cipher.getInstance("RSA");// Cipher.getInstance("RSA/ECB/PKCS1Padding");

		PublicKey pubKey = getPublicKey(publicKey);

		// 加密
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] deBytes = cipher.doFinal(code.getBytes(charSet));
		return deBytes;
	}
	
	/** 
     * 方式一 
     *  
     * @param bytes 
     * @return 
     */  
    public static String bytes2hex01(byte[] bytes)  
    {  
        /** 
         * 第一个参数的解释，记得一定要设置为1 
         *  signum of the number (-1 for negative, 0 for zero, 1 for positive). 
         */  
        BigInteger bigInteger = new BigInteger(1, bytes);  
        return bigInteger.toString(16);  
    }

	public static void main(String[] args) throws Exception {

		Map<String, String> keys = generateKeys();

		String pubKey = keys.get(PUBLIC_KEY);
		String priKey = keys.get(PRIVATE_KEY);
		System.out.println("pubKey:\n" + pubKey);
		System.out.println("priKey:\n" + priKey);
		
		String msg = "i am message!";
		
		byte[] encode = encode(msg, pubKey, "UTF-8");
		System.out.println("密文:\n"+bytes2hex01(encode));
		
		String decode = decode(encode, priKey, "UTF-8");
		System.out.println("解密:\n"+decode);

	}

}
