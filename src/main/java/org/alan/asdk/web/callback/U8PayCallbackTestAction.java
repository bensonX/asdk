package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 这个是PackServer通知游戏服的测试Action
 * Created by ant on 2015/2/9.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/callback")
public class U8PayCallbackTestAction extends UActionSupport {


    @Action("payCallback")
    public void payCallback() {

        try {

            Log.d("Now pay callback to packserver.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
