package org.alan.asdk.cache;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet初始化
 *
 * @author Lance Chow
 * @create 2016-05-20 15:43
 */
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        ServletContext context = servletContextEvent.getServletContext();
//        String fileName = FileHelper.getInstance().getPropertyMap("application.properties").get("Language.active");
//        LanguageCache.INS.load("files/" +fileName,context);
//        context.setAttribute("languageFileName",fileName);
//        Log.i("-----加载当前语言文件["+fileName+"]成功!----");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
