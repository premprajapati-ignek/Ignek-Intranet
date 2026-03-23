<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

<portlet:actionURL name="addUpdateEmployee" var="addEmployeeActionURL"/>

<%
    String employeeId = renderRequest.getParameter("employeeId");
    String firstName = renderRequest.getParameter("firstName");
    String lastName = renderRequest.getParameter("lastName");
    String designation = renderRequest.getParameter("designation");
    String emailAddress = renderRequest.getParameter("emailAddress");
    String phoneNumber = renderRequest.getParameter("phoneNumber");
    String addressLine1 = renderRequest.getParameter("addressLine1");
    String addressLine2 = renderRequest.getParameter("addressLine2");
    String city = renderRequest.getParameter("city");
    String zipCode = renderRequest.getParameter("zipCode");
%>

<div class="employee-form bg-white">
  <div class="m-2">
    <div class="container">
      <aui:form action="<%=addEmployeeActionURL %>" class="form-conta" name="employeeForm" method="POST">
        <aui:input name="employeeId" type="hidden" value="<%= GetterUtil.getLong(renderRequest.getParameter("employeeId")) %>" />
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
                <aui:input name="zipCode" label="zip-code" labelCssClass="poppins-regular" cssClass="form-control employee-form-input poppins-regular" placeholder="zip-code-placeholder" value="${employee.zipcode}">
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