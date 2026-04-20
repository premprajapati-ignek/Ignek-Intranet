<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<liferay-theme:defineObjects />

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
                            <%= (themeDisplay != null) ? HtmlUtil.escape(themeDisplay.getScopeGroupName()) : "IGNEK INTRANET" %>
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

                            <aui:button-row>
                                <aui:button cssClass="signIn-btn poppins-medium d-flex align-items-center justify-content-center w-100" type="submit" value="sign-in" />
                            </aui:button-row>
                        </aui:form>
                        <div
                        class="poppins-regular forgot-pass-txt d-flex align-items-center justify-content-center"
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

<style>
    .custom-login-container {
      background-color: #f0f1f1;
      min-height: 100vh;
      padding: 3rem 0;
    }
    .custom-login-container .login-display {
      height: 95vh;
    }
    .custom-login-container .login-form-container {
      padding: 0px 28px;
      background-color: #ffffff;
      width: 450px;
      border-radius: 20px;
      box-shadow: 2px 5px 10px 0px rgba(0, 0, 0, 0.1);
    }
    .custom-login-container .line {
      border: 5px solid #00979e;
      margin-right: 10px;
    }
    .custom-login-container .site-name {
      text-transform: uppercase;
      font-size: 30px;
      padding-top: 2.5rem;
      padding-left: 1rem;
    }
    .custom-login-container .signIn-heading-container {
      margin-top: 1.5rem;
    }
    .custom-login-container .signIn-txt {
      font-size: 22px;
    }
    .custom-login-container .signIn-description-txt {
      font-size: 14px;
      color: #6c6c6c;
    }
    .custom-login-container .signIn-form-container {
      padding-top: 2rem;
    }
    .custom-login-container .signIn-form-container .panel-body {
      padding: 0;
    }
    .custom-login-container .input-label-txt {
      color: #6c6c6c;
      font-size: 14px;
    }
    .custom-login-container .input-box {
      height: 44px;
      border: 1px solid #e5e5e5;
    }
    .custom-login-container .input-box::placeholder {
      color: #cdcdcd;
    }
    .custom-login-container .signIn-btn {
      margin-top: 1.3rem;
      background-color: #00979e;
      color: #ffffff;
      border-radius: 4px;
      border: none;
      height: 44px;
      transition: background-color 0.3s ease, transform 0.2s ease-in-out;
    }
    .custom-login-container .signIn-btn:hover {
      cursor: pointer;
      background-color: #007a80;
    }
    .custom-login-container .forgot-pass-txt {
      margin: 1.5rem 0;
    }
    .custom-login-container .reset-pass-link {
      padding-left: 5px;
    }
    .custom-login-container .reset-pass-link a {
      color: #00979e !important;
      text-decoration: underline;
    }
    .custom-login-container .reset-pass-link a:hover {
      color: #007a80 !important;
    }
    #content.container.flex-fill {
        background-color: #f0f1f1;
        margin: 0 !important;
        max-width: none;
    }
    #column-1 {
        padding: 0 !important;
    }
  </style>