<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<liferay-theme:defineObjects />

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css" />
<c:choose>
	<c:when test="<%= (themeDisplay != null) && themeDisplay.isSignedIn() %>">

		<%
		String signedInAs = HtmlUtil.escape(user.getFullName());

		if (themeDisplay.isShowMyAccountIcon() && (themeDisplay.getURLMyAccount() != null) && PortletPermissionUtil.contains(themeDisplay.getPermissionChecker(), 0, PortletKeys.MY_ACCOUNT, ActionKeys.ACCESS_IN_CONTROL_PANEL, true)) {
			String myAccountURL = String.valueOf(themeDisplay.getURLMyAccount());

			signedInAs = "<a class=\"signed-in\" href=\"" + HtmlUtil.escape(myAccountURL) + "\">" + signedInAs + "</a>";
		}
		%>

		<liferay-ui:message arguments="<%= signedInAs %>" key="you-are-signed-in-as-x" translateArguments="<%= false %>" />
	</c:when>
	<c:otherwise>

		<%
		String formName = "loginForm";

		if (windowState.equals(LiferayWindowState.EXCLUSIVE)) {
			formName += "Modal";
		}

		String redirect = ParamUtil.getString(request, "redirect");

		String login = (String)SessionErrors.get(renderRequest, "login");

		if (Validator.isNull(login)) {
			login = LoginUtil.getLogin(request, "login", company);
		}

		String password = StringPool.BLANK;
		boolean rememberMe = ParamUtil.getBoolean(request, "rememberMe");

		if (Validator.isNull(authType)) {
			authType = company.getAuthType();
		}
		%>

		<div class="login-container">
			<portlet:actionURL name="/login/login" secure="<%= request.isSecure() %>" var="loginURL">
				<portlet:param name="mvcRenderCommandName" value="/login/login" />
			</portlet:actionURL>

            <div class="custom-login-container w-100">
                <div
                    class="login-display container d-flex justify-content-center align-items-center"
                >
                    <div class="login-form-container">
                    <div class="site-name">
                        <span class="line"></span>
                        <span class="poppins-bold">
                            IGNEK INTRANET
                        </span>
                    </div>
                    <div
                        class="signIn-heading-container d-flex flex-column justify-content-center align-items-center"
                    >
                        <span class="signIn-txt poppins-semibold"><liferay-ui:message key="sign-in" /></span>
                        <span class="poppins-regular signIn-description-txt"
                        ><liferay-ui:message key="sign-in-message" /></span
                        >
                    </div>
                    <div class="signIn-form-container">
                        <aui:form action="<%= loginURL %>" autocomplete='<%= PropsValues.COMPANY_SECURITY_LOGIN_FORM_AUTOCOMPLETE ? "on" : "off" %>' cssClass="sign-in-form" method="post" name="<%= formName %>" onSubmit="event.preventDefault();" validateOnBlur="<%= false %>">
                            <aui:input name="saveLastPath" type="hidden" value="<%= false %>" />
                            <aui:input name="redirect" type="hidden" value="<%= redirect %>" />
                            <aui:input name="doActionAfterLogin" type="hidden" value="<%= portletName.equals(PortletKeys.FAST_LOGIN) ? true : false %>" />

                            <div class="inline-alert-container lfr-alert-container"></div>

                            <liferay-util:dynamic-include key="com.liferay.login.web#/login.jsp#alertPre" />

                            <c:choose>
                                <c:when test='<%= SessionMessages.contains(request, "forgotPasswordSent") %>'>
                                    <div class="alert alert-success">
                                        <liferay-ui:message key="your-request-completed-successfully" />
                                    </div>
                                </c:when>
                                <c:when test='<%= SessionMessages.contains(request, "userAdded") %>'>

                                    <%
                                    String userEmailAddress = (String)SessionMessages.get(request, "userAdded");
                                    %>

                                    <div class="alert alert-success">
                                        <liferay-ui:message key="thank-you-for-creating-an-account" />

                                        <c:if test="<%= company.isStrangersVerify() %>">
                                            <liferay-ui:message arguments="<%= HtmlUtil.escape(userEmailAddress) %>" key="your-email-verification-code-was-sent-to-x" translateArguments="<%= false %>" />
                                        </c:if>

                                        <c:if test="<%= PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_ENABLED) %>">
                                            <c:choose>
                                                <c:when test="<%= PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.LOGIN_CREATE_ACCOUNT_ALLOW_CUSTOM_PASSWORD, PropsValues.LOGIN_CREATE_ACCOUNT_ALLOW_CUSTOM_PASSWORD) %>">
                                                    <liferay-ui:message key="use-your-password-to-login" />
                                                </c:when>
                                                <c:otherwise>
                                                    <liferay-ui:message arguments="<%= HtmlUtil.escape(userEmailAddress) %>" key="you-can-set-your-password-following-instructions-sent-to-x" translateArguments="<%= false %>" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:when test='<%= SessionMessages.contains(request, "userPending") %>'>

                                    <%
                                    String userEmailAddress = (String)SessionMessages.get(request, "userPending");
                                    %>

                                    <div class="alert alert-success">
                                        <liferay-ui:message arguments="<%= HtmlUtil.escape(userEmailAddress) %>" key="thank-you-for-creating-an-account.-you-will-be-notified-via-email-at-x-when-your-account-has-been-approved" translateArguments="<%= false %>" />
                                    </div>
                                </c:when>
                            </c:choose>

                            <c:if test="<%= PropsValues.SESSION_ENABLE_PERSISTENT_COOKIES && PropsValues.SESSION_TEST_COOKIE_SUPPORT %>">
                                <div class="alert alert-danger" id="<portlet:namespace />cookieDisabled" style="display: none;">
                                    <liferay-ui:message key="authentication-failed-please-enable-browser-cookies" />
                                </div>
                            </c:if>

                            <c:choose>
                                <c:when test="<%= company.isSendPasswordResetLink() %>">
                                    <liferay-ui:error exception="<%= AuthException.class %>" message="authentication-failed-due-to-incorrect-credentials-or-account-lockout" />
                                </c:when>
                                <c:otherwise>
                                    <liferay-ui:error exception="<%= AuthException.class %>" message="authentication-failed" />
                                </c:otherwise>
                            </c:choose>

                            <liferay-ui:error exception="<%= CompanyMaxUsersException.class %>" message="unable-to-log-in-because-the-maximum-number-of-users-has-been-reached" />
                            <liferay-ui:error exception="<%= CookieNotSupportedException.class %>" message="authentication-failed-please-enable-browser-cookies" />
                            <liferay-ui:error exception="<%= NoSuchUserException.class %>" message="authentication-failed" />
                            <liferay-ui:error exception="<%= PasswordExpiredException.class %>" message="your-password-has-expired" />
                            <liferay-ui:error exception="<%= UserEmailAddressException.MustNotBeNull.class %>" message="please-enter-an-email-address" />
                            <liferay-ui:error exception="<%= UserLockoutException.LDAPLockout.class %>" message="this-account-is-locked" />

                            <c:choose>
                                <c:when test="<%= company.isSendPasswordResetLink() %>">
                                    <liferay-ui:error exception="<%= UserLockoutException.PasswordPolicyLockout.class %>" message="authentication-failed-due-to-incorrect-credentials-or-account-lockout" />
                                </c:when>
                                <c:otherwise>
                                    <liferay-ui:error exception="<%= UserLockoutException.PasswordPolicyLockout.class %>" message="authentication-failed" />
                                </c:otherwise>
                            </c:choose>

                            <liferay-ui:error exception="<%= UserPasswordException.class %>" message="authentication-failed" />
                            <liferay-ui:error exception="<%= UserScreenNameException.MustNotBeNull.class %>" message="the-screen-name-cannot-be-blank" />

                            <liferay-util:dynamic-include key="com.liferay.login.web#/login.jsp#alertPost" />

                            <aui:fieldset>

                                <%
                                String loginLabel = null;

                                if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
                                    loginLabel = "email-address";
                                }
                                else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
                                    loginLabel = "screen-name";
                                }
                                else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
                                    loginLabel = "id";
                                }
                                %>

                                <aui:input
                                    label="Email"
                                    labelCssClass="poppins-medium input-label-txt"
                                    cssClass="clearable poppins-regular input-box"
                                    name="login"
                                    required="<%= true %>"
                                    showRequiredLabel="<%= false %>"
                                    type="text"
                                    value="<%= login %>"
                                    placeholder="login-email-placeholder"
                                >
                                    <c:if test="<%= authType.equals(CompanyConstants.AUTH_TYPE_EA) %>">
                                        <aui:validator name="email" />
                                    </c:if>
                                </aui:input>

                                <aui:input name="password" label="Password" labelCssClass="poppins-medium input-label-txt" cssClass="poppins-regular input-box" required="<%= true %>" showRequiredLabel="<%= false %>" type="password" value="<%= password %>" placeholder="login-password-placeholder"/>

                                <span id="<portlet:namespace />passwordCapsLockSpan" style="display: none;"><liferay-ui:message key="caps-lock-is-on"/></span>

                                <!-- <c:if test="<%= company.isAutoLogin() %>">
                                    <aui:input checked="<%= rememberMe %>" name="rememberMe" type="checkbox" />
                                </c:if> -->
                            </aui:fieldset>

                            <liferay-captcha:captcha />
                            <liferay-ui:error key="otp-expired" message="The One-Time OTP has expired. Please request a new one." />
                            <liferay-ui:error key="invalid-otp" message="The One-Time OTP you entered is incorrect. Please check and try again." />
                            <liferay-ui:error key="session-expired" message="Your session has expired due to inactivity. Please log in again." />
                            <liferay-ui:error key="user-not-found" message="No account matches the provided details. Please verify the information." />
                            <liferay-ui:error key="otp-verification-error" message="An error occurred while verifying your One-Time Password. Please try again later." />
                            <liferay-ui:error key="captcha-error" message="The CAPTCHA text you entered is incorrect. Please try again." />

                            <aui:button-row>
                                <aui:button cssClass="signIn-btn poppins-medium d-flex align-items-center justify-content-center w-100" type="submit" value="sign-in" />
                            </aui:button-row>
                        </aui:form>
                        <div
                        class="poppins-regular d-flex align-items-center justify-content-center"
                        >
                        <liferay-ui:message key="forgot-password-message" />
                        <span class="reset-pass-link poppins-semibold"
                            ><%@ include file="/navigation.jspf" %></span
                        >
                        </div>
                    </div>
                    </div>
                </div>
            </div>
		</div>

		<aui:script sandbox="<%= true %>">
			var form = document.getElementById('<portlet:namespace /><%= formName %>');

			if (form) {
				form.addEventListener('submit', (event) => {
					<c:if test="<%= PropsValues.SESSION_ENABLE_PERSISTENT_COOKIES && PropsValues.SESSION_TEST_COOKIE_SUPPORT %>">
						if (!navigator.cookieEnabled) {
							document.getElementById(
								'<portlet:namespace />cookieDisabled'
							).style.display = '';

							return;
						}
					</c:if>

					<c:if test="<%= Validator.isNotNull(redirect) %>">
						var redirect = form.querySelector('#<portlet:namespace />redirect');

						if (redirect) {
							var redirectVal = redirect.getAttribute('value');

							redirect.setAttribute('value', redirectVal + window.location.hash);
						}
					</c:if>

					submitForm(form);
				});

				var password = form.querySelector('#<portlet:namespace />password');

				if (password) {
					password.addEventListener('keypress', (event) => {
						Liferay.Util.showCapsLock(
							event,
							'<portlet:namespace />passwordCapsLockSpan'
						);
					});
				}
			}
		</aui:script>
	</c:otherwise>
</c:choose>