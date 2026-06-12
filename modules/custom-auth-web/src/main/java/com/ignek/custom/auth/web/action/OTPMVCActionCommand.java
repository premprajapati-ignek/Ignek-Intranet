package com.ignek.custom.auth.web.action;

import com.ignek.custom.auth.web.constants.CustomAuthWebPortletKeys;
import com.liferay.login.web.constants.LoginPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.session.AuthenticatedSessionManagerUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component(
        property = {
                "javax.portlet.name=" + LoginPortletKeys.LOGIN,
                "mvc.command.name=/login/verify_otp"
        },
        service = MVCActionCommand.class
)

public class OTPMVCActionCommand extends BaseMVCActionCommand {

    private static final Log log = LogFactoryUtil.getLog(OTPMVCActionCommand.class);

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        HttpServletRequest request = PortalUtil.getOriginalServletRequest(
                PortalUtil.getHttpServletRequest(actionRequest));
        HttpSession session = request.getSession();

        try {
            String enteredOtp = ParamUtil.getString(actionRequest, "otp");
            String storedOtp = (String)session.getAttribute(CustomAuthWebPortletKeys.OTP);

            if (Validator.isNull(storedOtp)) {
                log.info("No OTP found in session");
                SessionErrors.add(actionRequest, "otp-expired");
                actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_LOGIN);
                return;
            }

            if (!storedOtp.equals(enteredOtp)) {
                log.warn("Invalid OTP entered");
                SessionErrors.add(actionRequest, "invalid-otp");
                actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_LOGIN);
                return;
            }

            String login = (String)session.getAttribute("login");
            String password = (String)session.getAttribute("password");

            if (Validator.isNull(login) || Validator.isNull(password)) {
                log.error("Login credentials not found in session after OTP verification");
                SessionErrors.add(actionRequest, "session-expired");
                actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_LOGIN);
                return;
            }

            long companyId = PortalUtil.getCompanyId(request);
            User user = userLocalService.getUserByEmailAddress(companyId, login);

            if (user == null) {
                log.error("No user found with email address: " + login);
                    SessionErrors.add(actionRequest, "user-not-found");
                actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_LOGIN);
                return;
            }

            log.info("OTP verified successfully for user: " + user.getEmailAddress());
            session.removeAttribute("OTP");

            HttpServletResponse response = PortalUtil.getHttpServletResponse(actionResponse);

            AuthenticatedSessionManagerUtil.login(
                    request,
                    response,
                    user.getEmailAddress(),
                    password,
                    false,
                    null);

            log.info("User logged in successfully: " + user.getEmailAddress());
            actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_DASHBOARD);
        }
        catch (Exception exception) {
            log.error("Unexpected error while processing OTP verification", exception);
            SessionErrors.add(actionRequest, "otp-verification-error");
            actionResponse.sendRedirect(CustomAuthWebPortletKeys.REDIRECT_TO_LOGIN);
        }
    }

    @Reference
    private UserLocalService userLocalService;
}