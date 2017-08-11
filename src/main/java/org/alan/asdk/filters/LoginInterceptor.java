package org.alan.asdk.filters;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ant on 2015/8/29.
 */
public class LoginInterceptor extends MethodFilterInterceptor {


    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {

        HttpServletRequest request = ServletActionContext.getRequest();

        String path = request.getRequestURI();
        try {
            Map params = request.getParameterMap();
            JSONObject js = JSONObject.fromObject(params);
            Log.d("The path is" + path + " | params is " + js.toString());
        } catch (Exception e) {
            Log.d("The path is" + path + " | e....");
        }

        if (!path.startsWith("/admin")) {
            return actionInvocation.invoke();
        }

        if (actionInvocation.getAction() instanceof UActionSupport) {
            UActionSupport action = (UActionSupport) actionInvocation.getAction();
            Map session = action.getSession();
            if (session.get("admin") != null) {
                return actionInvocation.invoke();
            }
        }
        return Action.LOGIN;
    }
}
