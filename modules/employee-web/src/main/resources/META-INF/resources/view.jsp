<%@ include file="/init.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ignek.employee.model.Employee" %>

<% List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList"); %>
<portlet:renderURL var="addEmployeeRenderURL">
    <portlet:param name="mvcPath" value="/add-update-employee.jsp"/>
</portlet:renderURL>

<div class="employees-container">
  <div class="container mt-4">
    <div class="d-flex justify-content-between">
      <p class="employee-title poppins-bold"><liferay-ui:message key="title-employees"/></p>
      <a
        class="add-employee-btn poppins-medium d-flex align-items-center justify-content-center"
        href="<%= addEmployeeRenderURL %>"
        ><liferay-ui:message key="add-new-employee-btn"/></a>
    </div>
    <hr />
    <table class="w-100">
      <tr class="poppins-semibold">
        <th><liferay-ui:message key="sr-no"/></th>
        <th><liferay-ui:message key="name"/></th>
        <th><liferay-ui:message key="designation"/></th>
        <th><liferay-ui:message key="phone"/></th>
        <th><liferay-ui:message key="email"/></th>
        <th><liferay-ui:message key="city"/></th>
        <th class="action-th"><liferay-ui:message key="actions"/></th>
      </tr>
      <c:forEach items="${employeeList}" var="employee">
        <portlet:resourceURL var="downloadEmployeePdfURL" id="/downloadEmployeePdf">
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
        </portlet:resourceURL>

        <portlet:renderURL var="updateEmployeeRenderURL">
            <portlet:param name="mvcPath" value="/add-update-employee.jsp"/>
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
            <portlet:param name="firstName" value="${employee.firstName}"/>
            <portlet:param name="lastName" value="${employee.lastName}"/>
            <portlet:param name="designation" value="${employee.designation}"/>
            <portlet:param name="phoneNumber" value="${employee.phoneNumber}"/>
            <portlet:param name="addressLine1" value="${employee.addressLine1}"/>
            <portlet:param name="addressLine2" value="${employee.addressLine2}"/>
            <portlet:param name="emailAddress" value="${employee.emailAddress}"/>
            <portlet:param name="city" value="${employee.city}"/>
            <portlet:param name="zipCode" value="${employee.zipCode}"/>
        </portlet:renderURL>

        <portlet:actionURL name="deleteEmployee" var="deleteEmployeeActionURL">
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
        </portlet:actionURL>

        <tr class="poppins-regular">
            <td>${employee.employeeId}</td>
            <td>${employee.firstName} ${employee.lastName}</td>
            <td>${employee.designation}</td>
            <td>${employee.phoneNumber}</td>
            <td>${employee.emailAddress}</td>
            <td>${employee.city}</td>
            <td class="action-td">
              <a href="<%=updateEmployeeRenderURL%>" class="action-link"
                ><img class="action-icon-img" src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/images/edit.png") %>" alt="#"
              /></a>

              <a href="javascript:void(0);" class="action-link"
                 onclick="openDeletePopup('<%= deleteEmployeeActionURL %>')">
                 <img class="action-icon-img"  src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/images/delete.png") %>" alt="#"/>
              </a>

              <a href="<%= downloadEmployeePdfURL %>" class="action-link"
                ><img
                  class="action-icon-img"
                   src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/images/pdfDownload.png") %>" alt="#"
              /></a>
            </td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>

<div id="deleteModal" class="custom-modal">
  <div class="modal-content d-flex justify-content-between">
    <p class="modal-message poppins-medium">
      <liferay-ui:message key="delete-modal-message"/>
    </p>
    <div class="d-flex flex-row-reverse">
      <button class="modal-btn poppins-medium" onclick="confirmDelete()">
        <liferay-ui:message key="yes-btn"/>
      </button>
      <button class="modal-btn poppins-medium" onclick="closePopup()">
        <liferay-ui:message key="no-btn"/>
      </button>
    </div>
  </div>
</div>

<script>
function openDeletePopup(url) {
    deleteUrl = url;
    document.getElementById("deleteModal").style.display = "block";
}

function closePopup() {
    document.getElementById("deleteModal").style.display = "none";
}

function confirmDelete() {
    window.location.href = deleteUrl;
}
</script>
