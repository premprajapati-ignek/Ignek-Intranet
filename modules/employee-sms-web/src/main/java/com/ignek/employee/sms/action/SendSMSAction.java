package com.ignek.employee.sms.action;

import com.ignek.employee.sms.constants.EmployeeSmsPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.osgi.service.component.annotations.Component;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
    property = {
        "javax.portlet.name=" + EmployeeSmsPortletKeys.HR_SMS_WEB,
        "mvc.command.name=/sendSMS"
    },
    service = MVCActionCommand.class
)

public class SendSMSAction extends BaseMVCActionCommand {
    private static final Log log = LogFactoryUtil.getLog(SendSMSAction.class);

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {
        String accountSid = ParamUtil.getString(actionRequest, EmployeeSmsPortletKeys.ACCOUNT_SID);
        String authToken = ParamUtil.getString(actionRequest, EmployeeSmsPortletKeys.AUTH_TOKEN);
        String body = ParamUtil.getString(actionRequest, EmployeeSmsPortletKeys.BODY);
        String from = ParamUtil.getString(actionRequest, EmployeeSmsPortletKeys.FROM);
        String to = ParamUtil.getString(actionRequest, EmployeeSmsPortletKeys.TO);

        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body
                    ).create();
            log.info("SMS sent successfully, SID: " + message.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS", e);
        }
    }
}
