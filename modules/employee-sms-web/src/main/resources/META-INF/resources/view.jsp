<%@ include file="/init.jsp" %>

<portlet:actionURL name="/sendSMS" var="sendSMSActionURL"/>

<div class="settings-frame">
<p class="poppins-semibold settings-form-heading">SMS Settings</p>
<aui:form action="${sendSMSActionURL}" class="settings-form" name="employeeSMSForm" method="POST">
  <div class="input-container">
      <aui:input
          cssClass="poppins-regular"
          id="accountSid"
          label="accountSid"
          labelCssClass="poppins-regular"
          name="accountSid"
          value="msg-twilio-account-sid"
          placeholder="ACXXXXXXXXXXXXXXXXXXXXX"
          type="text">
            <aui:validator name="required" errorMessage="Please enter your accountSid." />
      </aui:input>
  </div>

  <div class="input-container">
    <aui:input
      cssClass="poppins-regular"
      id="authToken"
      label="authToken"
      labelCssClass="poppins-regular"
      name="authToken"
      value="msg-twilio-auth-token"
      placeholder="581234876547"
      type="text">
        <aui:validator name="required" errorMessage="Please enter your authToken." />
    </aui:input>
  </div>

  <div class="input-container">
    <aui:input
      cssClass="poppins-regular"
      id="body"
      label="body"
      labelCssClass="poppins-regular"
      name="body"
      placeholder="SMS sent successfully"
      type="text">
        <aui:validator name="required" errorMessage="Please enter SMS body." />
    </aui:input>
  </div>

  <div class="input-container">
    <aui:input
        cssClass="poppins-regular"
        id="from"
        label="from"
        labelCssClass="poppins-regular"
        name="from"
        placeholder="+45366464628292"
        type="text">
            <aui:validator name="required" errorMessage="Please enter from phone number." />
    </aui:input>
  </div>

  <div class="input-container">
    <aui:input
      cssClass="poppins-regular"
      id="to"
      label="to"
      labelCssClass="poppins-regular"
      name="to"
      placeholder="+45283874646473"
      type="text">
        <aui:validator name="required" errorMessage="Please enter to phone number." />
    </aui:input>
  </div>

  <div class="btn-row d-flex justify-content-center align-items-center">
      <aui:button
        cssClass="send-sms-btn poppins-semibold"
        name="sendSmsButton"
        type="submit"
        value="Send Test SMS"
      />

      <aui:button
          id="cancelButtonId"
          cssClass="cancle-btn poppins-semibold"
          name="cancelButton"
          type="button"
          value="Cancle"
      />
  </div>
</aui:form>
</div>
