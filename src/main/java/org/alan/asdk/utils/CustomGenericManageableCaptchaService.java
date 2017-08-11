package org.alan.asdk.utils;

import java.awt.image.BufferedImage;
import java.util.Locale;

public class CustomGenericManageableCaptchaService  {

    public BufferedImage getImageChallengeForID(String captchaId, Locale locale) {
        return new BufferedImage(1,2,3);
    }

    public boolean validateResponseForID(String id, String captcha) {
        return true;
    }
}
