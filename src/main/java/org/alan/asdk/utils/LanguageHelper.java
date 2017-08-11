package org.alan.asdk.utils;

import org.alan.asdk.common.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 语言工具类
 *
 * @author Lance Chow
 * @create 2016-07-28 14:48
 */
public enum  LanguageHelper {

    INS;

    private static Map<String,Map<String,String>> languageMap;

    public void setLanguage(HttpServletRequest request){
        //默认加载英语配置
        //获取语言
        String language = getDeviceLanguage(request);
        Set<String> s = getLanguageMap(language).keySet();
        for (String s1 : s) {
            request.setAttribute(s1,languageMap.get(language).get(s1));
        }
        request.setAttribute("languageFileName",language);
    }

    public Map<String,String> getLanguageMap(String language){
        Map<String,String> map = languageMap.get(language);
        if (map == null){
            map = new HashMap<>();
        }
        return map;
    }


    public String getDeviceLanguage(HttpServletRequest request){
        String language = request.getParameter("language");

        if (language == null){
            language = request.getHeader("Accept-Language");
        }
        if (language == null){
            language = "en_us";
        }
        language = language.toLowerCase();
        if (language.contains("cn") || language.contains("zh")){
            language = "zh_cn";
        }
        else if(language.contains("vi")){
            language = "vi";
        }
        else if (language.contains("th")){
            language = "th";
        }
        if (!language.equals("zh_cn")&&!language.equals("vi")&&!language.equals("th")){
            language = "en_us";
        }
        if (languageMap.get(language) == null){
            language = "en_us";
        }
        return language;
    }

    public void loadLanguage(){
        languageMap = new HashMap<>();
//        File dir = new File(FileHelper.getInstance().getRealPath("files"));
//        for (File file : dir.listFiles()) {
//            String name = file.getName();
//            if (name.endsWith("properties")){
//                Map<String,String>  map = FileHelper.getInstance().getPropertyMap("files/"+name);
//                languageMap.put(name.substring(0,name.lastIndexOf(".")),map);
//            }
//        }
        Log.i("load "+languageMap.size()+" language files success!");
    }
}
