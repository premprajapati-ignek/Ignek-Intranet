package com.ignek.custom.auth.web.action;

import com.ignek.custom.auth.web.constants.CustomAuthWebPortletKeys;
import com.ignek.custom.auth.web.util.MailUtil;
import com.liferay.captcha.util.CaptchaUtil;
import com.liferay.login.web.constants.LoginPortletKeys;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.captcha.CaptchaException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Component(
        property = {
                "javax.portlet.name=" + LoginPortletKeys.LOGIN,
                "mvc.command.name=/login/login",
                "service.ranking:Integer=100"
        },
        service = MVCActionCommand.class
)

public class LoginMVCActionCommand extends BaseMVCActionCommand {
    private static final Log log = LogFactoryUtil.getLog(LoginMVCActionCommand.class);

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        try {
            CaptchaUtil.check(actionRequest);
            log.info("captcha check success");

            String login = actionRequest.getParameter(CustomAuthWebPortletKeys.LOGIN);
            String password = actionRequest.getParameter(CustomAuthWebPortletKeys.PASSWORD);
            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            HttpServletRequest request =
                    PortalUtil.getOriginalServletRequest(
                            PortalUtil.getHttpServletRequest(actionRequest));
            HttpSession session = request.getSession();

            session.setAttribute(CustomAuthWebPortletKeys.LOGIN, login);
            session.setAttribute(CustomAuthWebPortletKeys.PASSWORD, password);
            session.setAttribute(CustomAuthWebPortletKeys.OTP, otp);

            log.info("Stored OTP in session: " + session.getAttribute(CustomAuthWebPortletKeys.OTP));
            MailUtil.sendOTP(mailService, login, otp);
            log.info("OTP sent successfully to " + login);

            actionResponse.getRenderParameters().setValue("mvcPath","/otp.jsp");
        }
        catch (CaptchaException ce){
            SessionErrors.add(actionRequest, "captcha-error");
            log.info("Captcha exception: " + ce.getMessage());
        }
        catch (Exception e) {
            log.error("An error occurred during the captcha validation process", e);
        }
    }

    @Reference
    private MailService mailService;
}
