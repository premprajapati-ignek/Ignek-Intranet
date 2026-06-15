package com.ignek.custom.auth.web.util;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import javax.mail.internet.InternetAddress;

public class MailUtil {
    public static final Log log = LogFactoryUtil.getLog(MailUtil.class);

    public static void sendOTP(MailService mailService, String toEmail, String otp) {
        try {
            MailMessage mailMessage = new MailMessage();

            mailMessage.setFrom(new InternetAddress("prem.prajapati.ignek@gmail.com"));
            mailMessage.setTo(new InternetAddress(toEmail));
            mailMessage.setSubject("OTP Verification");
            mailMessage.setBody("Your OTP is: " + otp);
            mailMessage.setHTMLFormat(false);

            mailService.sendEmail(mailMessage);
        }
        catch (Exception e) {
            log.error("Unable to send OTP email", e);
        }
    }
}