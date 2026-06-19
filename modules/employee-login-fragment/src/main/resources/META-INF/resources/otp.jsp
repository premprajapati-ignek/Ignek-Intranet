<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<liferay-theme:defineObjects />
<portlet:defineObjects />

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/otp.css" type="text/css" />

<portlet:actionURL name="/login/verify_otp" var="verifyOTPURL" />

<div class="login-otp-container w-100">
  <div
    class="login-display container d-flex justify-content-center align-items-center"
  >
    <div class="login-form-container">
      <div class="site-name">
          <span class="line"></span>
          <span class="poppins-bold">
              <liferay-ui:message key="ignek-intranet" />
          </span>
      </div>

      <div
          class="signIn-heading-container d-flex flex-column justify-content-center align-items-center"
      >
          <span class="signIn-txt poppins-semibold">
              <liferay-ui:message key="sign-in" />
          </span>

          <span class="poppins-regular signIn-description-txt">
              <liferay-ui:message key="enter-otp-to-access-your-account" />
          </span>
      </div>

      <div class="signIn-form-container">
          <aui:form action="<%= verifyOTPURL %>" method="post">

              <div class="form-group">
                  <aui:input
                      label="otp"
                      placeholder="enter-otp"
                      cssClass="form-control poppins-regular input-box"
                      name="otp">

                      <aui:validator
                          name="required"
                          errorMessage="please-enter-otp" />
                  </aui:input>
              </div>

              <aui:button
                  cssClass="verify-btn poppins-medium d-flex align-items-center justify-content-center w-100 mb-4"
                  name="verifyButton"
                  type="submit"
                  value="verify-otp"
              />

          </aui:form>
      </div>
    </div>
  </div>
</div>