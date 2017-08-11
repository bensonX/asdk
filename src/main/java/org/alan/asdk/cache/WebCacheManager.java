package org.alan.asdk.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络缓存
 *
 * @author Lance Chow
 * @create 2015-12-18 15:12
 */
public class WebCacheManager {

    private static Map<String,String> mailCaptcha;

    public void saveMailCaptcha(String str , String captcha){
        if (mailCaptcha.containsKey(str)){
            mailCaptcha.remove(str);
        }
        mailCaptcha.put(str,captcha);
    }

    public String getMailCaptcha(String str){
        return mailCaptcha.get(str);
    }

    public void removeMailCaptcha(String str){
        if (mailCaptcha.containsKey(str)){
            mailCaptcha.remove(str);
        }
    }

    public void loadMailCaptcha(){
        if (mailCaptcha==null){
            mailCaptcha = new HashMap<String, String>();
        }
    }

    private static WebCacheManager instance;

    public static WebCacheManager getInstance(){
        if (instance == null){
            instance = new WebCacheManager();
        }
        return instance;
    }

}
