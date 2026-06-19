<%@ include file="/init.jsp" %>

<portlet:actionURL name="/sendSMS" var="sendSMSActionURL"/>

<div class="settings-frame">
    <p class="poppins-semibold settings-form-heading">
        <liferay-ui:message key="sms-settings" />
    </p>

    <aui:form action="${sendSMSActionURL}" class="settings-form" name="employeeSMSForm" method="POST">

        <div class="input-container">
            <aui:input
                cssClass="poppins-regular"
                id="accountSid"
                label="account-sid"
                labelCssClass="poppins-regular"
                name="accountSid"
                value="msg-twilio-account-sid"
                placeholder="account-sid-placeholder"
                type="text">

                <aui:validator name="required"
                    errorMessage="please-enter-account-sid" />
            </aui:input>
        </div>

        <div class="input-container">
            <aui:input
                cssClass="poppins-regular"
                id="authToken"
                label="auth-token"
                labelCssClass="poppins-regular"
                name="authToken"
                value="msg-twilio-auth-token"
                placeholder="auth-token-placeholder"
                type="text">

                <aui:validator name="required"
                    errorMessage="please-enter-auth-token" />
            </aui:input>
        </div>

        <div class="input-container">
            <aui:input
                cssClass="poppins-regular"
                id="body"
                label="sms-body"
                labelCssClass="poppins-regular"
                name="body"
                placeholder="sms-body-placeholder"
                type="text">

                <aui:validator name="required"
                    errorMessage="please-enter-sms-body" />
            </aui:input>
        </div>

        <div class="input-container">
            <aui:input
                cssClass="poppins-regular"
                id="from"
                label="from-phone-number"
                labelCssClass="poppins-regular"
                name="from"
                placeholder="from-phone-number-placeholder"
                type="text">

                <aui:validator name="required"
                    errorMessage="please-enter-from-phone-number" />
            </aui:input>
        </div>

        <div class="input-container">
            <aui:input
                cssClass="poppins-regular"
                id="to"
                label="to-phone-number"
                labelCssClass="poppins-regular"
                name="to"
                placeholder="to-phone-number-placeholder"
                type="text">

                <aui:validator name="required"
                    errorMessage="please-enter-to-phone-number" />
            </aui:input>
        </div>

        <div class="btn-row d-flex justify-content-center align-items-center">
            <aui:button
                cssClass="send-sms-btn poppins-semibold"
                name="sendSmsButton"
                type="submit"
                value="send-test-sms"
            />

            <aui:button
                id="cancelButtonId"
                cssClass="cancle-btn poppins-semibold"
                name="cancelButton"
                type="button"
                value="cancel"
            />
        </div>

    </aui:form>
</div>