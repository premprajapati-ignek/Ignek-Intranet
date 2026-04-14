<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.ignek.employee.model.Employee" %>
<%@ page import="com.ignek.employee.service.EmployeeLocalServiceUtil" %>

<portlet:actionURL name="addUpdateEmployee" var="addEmployeeActionURL"/>

<div class="employee-form bg-white">
  <div class="m-2">
    <div class="container">
      <aui:form action="${addEmployeeActionURL}" class="form-conta" name="employeeForm" method="POST">
        <aui:input name="employeeId" type="hidden" value="${employee.employeeId}" />
        <div class="row employee-form-group">
          <div class="col-12">
            <p class="form-heading poppins-bold mb-3"><liferay-ui:message key="title-employee-form"/></p>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="firstName" labelCssClass="poppins-regular" label="first-name" cssClass="form-control employee-form-input poppins-regular" placeholder="first-name-placeholder" value="${employee.firstName}">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="lastName" label="last-name" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="last-name-placeholder" value="${employee.lastName}">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-12">
            <div class="form-group">
                <aui:input name="designation" label="designation" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="designation-placeholder" value="${employee.designation}">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="emailAddress" label="email" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="email-placeholder" value="${employee.emailAddress}">
                    <aui:validator name="email"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="phoneNumber" label="phone" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="phone-number-placeholder" value="${employee.phoneNumber}">
                    <aui:validator name="digits"/>
                    <aui:validator name="maxLength">10</aui:validator>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="addressLine1" label="address-line-1" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="address-line-1-placeholder" value="${employee.addressLine1}">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="addressLine2" label="address-line-2" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="address-line-2-placeholder" value="${employee.addressLine2}">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="city" label="city" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="city-placeholder" value="${employee.city}">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="zipCode" label="zip-code" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="zip-code-placeholder" value="${employee.zipCode}">
                    <aui:validator name="digits"/>
                    <aui:validator name="maxLength">6</aui:validator>
                </aui:input>
            </div>
          </div>
        </div>
        <div class="d-flex flex-row-reverse">
          <aui:button type="submit" name="submit" value="submit-btn" cssClass="employee-form-btn poppins-medium"></aui:button>
        </div>
      </aui:form>
    </div>
  </div>
</div>