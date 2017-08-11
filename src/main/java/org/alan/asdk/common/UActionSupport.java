package org.alan.asdk.common;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 所有Action继承该类
 */
public class UActionSupport extends ActionSupport
        implements ServletRequestAware, SessionAware, ServletResponseAware {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Map session;
    private String scripts;
    private final Log logger = LogFactory.getLog(getClass());

    private void render(String content, String contentType) {
        try {
            HttpServletResponse localHttpServletResponse = ServletActionContext.getResponse();
            if (localHttpServletResponse != null) {
                localHttpServletResponse.setHeader("Pragma", "No-cache");
                localHttpServletResponse.setHeader("Cache-Control", "no-cache");
                localHttpServletResponse.setDateHeader("Expires", 0L);
                localHttpServletResponse.setContentType(contentType);
                localHttpServletResponse.getWriter().write(content);
            }
        } catch (IOException localIOException) {
            this.logger.error(localIOException.getMessage(), localIOException);
        }
    }

    /**
     * 获取反向代理之后的客户端真实IP
     * X-Real-IP 由于使用了熊林的反向代理 所以将默认值 x-forwarded-for 改为熊林的预设值 X-Real-IP
     * @return IP地址
     */
    public String getRemortRealIP() {

        if (this.request.getHeader("X-Real-IP") == null) {//X-Real-IP 这里默认值为 x-forwarded-for
            return this.request.getRemoteAddr();
        }
        return this.request.getHeader("X-Real-IP");
    }
    public String getRemoteHost(){
        String ip = this.request.getHeader("X-Real-IP");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = this.request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = this.request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = this.request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

    protected boolean isPost() {
        return "POST".equals(this.request.getMethod());
    }

    protected void renderText(String content) {
        render(content, "text/plain; charset=UTF-8");
    }

    protected void renderJson(String content) {
        render(content, "text/json; charset=UTF-8");
    }

    protected void renderHtml(String content) {
        render(content, "text/html; charset=UTF-8");
    }

    protected void renderXml(String content) {
        render(content, "text/xml; charset=UTF-8");
    }

    protected void putIntoSession(String key, Object value) {
        this.session.put(key, value);
    }

    public void setServletRequest(HttpServletRequest req) {
        this.request = req;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map getSession() {
        return this.session;
    }

    public String getScripts() {
        return this.scripts;
    }

    public void setScripts(String scripts) {
        this.scripts = scripts;
    }
}
