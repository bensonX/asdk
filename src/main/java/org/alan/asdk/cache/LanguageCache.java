package org.alan.asdk.cache;

import org.alan.asdk.utils.LanguageHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * 国际化
 *
 * @author Lance Chow
 * @create 2016-05-19 11:19
 */
public enum  LanguageCache {

    INS;

    public String get(String key , HttpServletRequest request){
        String language = LanguageHelper.INS.getDeviceLanguage(request);
        return LanguageHelper.INS.getLanguageMap(language).get(key);
    }




}
