package org.alan.asdk.sdk.soha;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class CommonDAO {


	public String parse_signed_request(String signed_request, String secret) throws ParseException, IOException {
		
		String result = null;
		String payloadDecode = null;
		// list($encoded_sig, $payload) = explode('.', $signed_request, 2);
		String[] arr = signed_request.split("\\.");
		if(arr != null && arr.length > 0){
			String encoded_sig = arr[0];
			String payload = arr[1];
			// strtr($encoded_sig, '-_', '+/')
			//String sigBefore = encoded_sig.replace("-_", "+/");
			// strtr($payload, '-_', '+/')
			String payloadBefore = payload.replace("-_", "+/");
			// decode sig
			//String sigDecode = StringUtils.newStringUtf8(Base64.decodeBase64(sigBefore));
			// deocde payload
			payloadDecode = StringUtils.newStringUtf8(Base64.decodeBase64(payloadBefore));
			//comvert json object
			Base64 decoder = new Base64();
			encoded_sig = encoded_sig.replaceAll("\\-", "\\+");
		    byte[] signature = decoder.decode(encoded_sig);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(payloadDecode);
			JSONObject jsonObject = (JSONObject) obj;
			
			//check algorithm sha256
			if (!jsonObject.get("algorithm").toString().toUpperCase().equals("HMAC-SHA256")) {
				System.out.println("Unknown algorithm. Expected HMAC-SHA256");
				return "fail";
			}
			
			//String expected_sig = hmacDigest(payload, secret, "HmacSHA256");
			byte[] expected_sig = hmacDigest(payload, secret, "HmacSHA256");
			//if(!signature.equals(expected_sig)){
			if(!ArrayCompare(signature, expected_sig)){
				return "fail";
			}
		}else{
			return "fail";
		}
		
		return payloadDecode;
	}

	public static void main(String[] args) throws ParseException, IOException {
		CommonDAO dao =  new CommonDAO();
		System.out.println(dao.parse_signed_request("fbh6g9uTj1bT5xTnE-yQpfWdypRjVW7Qe_ptSdToU3A=.eyJhcHBfaWQiOiIzOThlMWQ5ZWI0OTI0ZWM3Yjg0Y2RkYjhjNjQ5NTc2YyIsInVzZXJfaWQiOiI4MzMwMjEyMTUiLCJvcmRlcl9pbmZvIjoic2cxMzZfc2NfMTAiLCJyb2xlX2lkIjoiMDEwMDAwNSIsImFyZWFfaWQiOiIxIiwib3JkZXJfaWQiOiI5OTk1NzE0NzUwNDU0NDMiLCJwcmljZSI6IjEwIiwidGltZSI6MTQ3NTA0NTQ0MywiYWxnb3JpdGhtIjoiSE1BQy1TSEEyNTYifQ==","575dcc83da29a842ea5c3c337fca2c27"));


	}
	
	public static byte[] hmacDigest(String msg, String keyString, String algo) {
		byte[] digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec(
					(keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
			/*
			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			
			digest = hash.toString();
			*/
			digest = bytes;
			return digest;
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return digest;
	}
	
	public static String resultJsonEncode(String status,String msg) throws IOException{
		JSONObject obj = new JSONObject();

		 if(status.equals("success")){
			 obj.put("status","settled");
			 obj.put("message",msg);
		 }else if(status.equals("failed")){
			 obj.put("status","failed");
			 obj.put("message",msg);
		 }

	      StringWriter out = new StringWriter();
	      obj.writeJSONString(out);
	      
	      String jsonText = out.toString();
	      return jsonText;
	}
	
	public static boolean ArrayCompare(byte[] a, byte[] a2) {
	    if (a==a2)   // checks for same array reference
	        return true;
	    if (a==null || a2==null)  // checks for null arrays
	        return false;

	    int length = a.length;
	    if (a2.length != length)  // arrays should be of equal length
	        return false;

	    for (int i=0; i<length; i++)  // compare array values
	        if (a[i] != a2[i])
	            return false;

	    return true;
	}
	 
}
