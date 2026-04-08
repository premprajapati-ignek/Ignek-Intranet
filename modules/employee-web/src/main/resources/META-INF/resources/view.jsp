<%@ include file="/init.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ignek.employee.model.Employee" %>

<% List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList"); %>
<portlet:renderURL var="addEmployeeRenderURL">
    <portlet:param name="mvcPath" value="/add-update-employee.jsp"/>
</portlet:renderURL>
<c:url var="editEmployeeIcon" value="/images/edit.png" />
<c:url var="deleteEmployeeIcon" value="/images/delete.png" />
<c:url var="pdfDownloadIcon" value="/images/pdfDownload.png" />

<div class="employees-container">
  <div class="container mt-4">
    <div class="d-flex justify-content-between">
      <p class="employee-title poppins-bold"><liferay-ui:message key="title-employees"/></p>
      <c:if test="${not isSiteEmployee}">
        <a class="add-employee-btn poppins-medium d-flex align-items-center justify-content-center"
            href="${addEmployeeRenderURL}"
            ><liferay-ui:message key="add-new-employee-btn"/></a>
      </c:if>
    </div>
    <hr />
    <table class="w-100">
      <tr class="poppins-semibold">
        <th class="text-th-center"><liferay-ui:message key="sr-no"/></th>
        <th><liferay-ui:message key="name"/></th>
        <th><liferay-ui:message key="designation"/></th>
        <th><liferay-ui:message key="phone"/></th>
        <th><liferay-ui:message key="email"/></th>
        <th><liferay-ui:message key="city"/></th>
        <th class="text-th-center"><liferay-ui:message key="actions"/></th>
      </tr>
      <c:forEach items="${employeeList}" var="employee" varStatus="loopStatus">
        <portlet:resourceURL var="downloadEmployeePdfURL" id="/downloadEmployeePdf">
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
        </portlet:resourceURL>

        <portlet:renderURL var="updateEmployeeRenderURL">
            <portlet:param name="mvcPath" value="/add-update-employee.jsp"/>
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
        </portlet:renderURL>

        <portlet:actionURL name="deleteEmployee" var="deleteEmployeeActionURL">
            <portlet:param name="employeeId" value="${employee.employeeId}"/>
        </portlet:actionURL>

        <tr class="poppins-regular">
            <td  class="text-td-center">${loopStatus.count}</td>
            <td>${employee.firstName} ${employee.lastName}</td>
            <td>${employee.designation}</td>
            <td>${employee.phoneNumber}</td>
            <td>${employee.emailAddress}</td>
            <td>${employee.city}</td>
            <td class="text-td-center">
              <c:if test="${not isSiteEmployee}">
                <a href="${updateEmployeeRenderURL}" class="action-link"
                    ><img class="action-icon-img" src="${editEmployeeIcon}" alt="#"
                  /></a>

                  <a href="javascript:void(0);" class="action-link"
                     onclick="openDeletePopup('${deleteEmployeeActionURL}')">
                     <img class="action-icon-img"  src="${deleteEmployeeIcon}" alt="#"/>
                  </a>
              </c:if>
              <a href="${downloadEmployeePdfURL}" target="_blank" class="action-link"
                ><img
                  class="action-icon-img"
                  src="${pdfDownloadIcon}" alt="#"
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