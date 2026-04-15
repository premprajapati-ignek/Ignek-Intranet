<%@ include file="/init.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@ page import="com.ignek.employee.model.Employee" %>

<%
    List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
    int totalEmployees = (employeeList != null) ? employeeList.size() : 0;
%>

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

    <liferay-ui:search-container
        delta="5"
        total="<%= totalEmployees %>"
        emptyResultsMessage="No Employees Found">

        <liferay-ui:search-container-results
            results="<%= ListUtil.subList(employeeList, searchContainer.getStart(), searchContainer.getEnd()) %>"/>

        <liferay-ui:search-container-row
            className="com.ignek.employee.model.Employee" modelVar="employee">

            <portlet:resourceURL var="downloadEmployeePdfURL" id="/downloadEmployeePdf">
                <portlet:param name="employeeId" value="${employee.employeeId}"/>
            </portlet:resourceURL>

            <portlet:renderURL var="updateEmployeeRenderURL">
                <portlet:param name="mvcRenderCommandName" value="updateEmployee" />
                <portlet:param name="employeeId" value="${employee.employeeId}" />
            </portlet:renderURL>

            <portlet:actionURL name="deleteEmployee" var="deleteEmployeeActionURL">
                <portlet:param name="employeeId" value="${employee.employeeId}"/>
            </portlet:actionURL>

            <liferay-ui:search-container-column-text cssClass="text-td-center poppins-regular" name="Sr No" value="${searchContainer.start + index + 1}" />
            <liferay-ui:search-container-column-text cssClass="poppins-regular" name="Name" value="${employee.firstName} ${employee.lastName}" />
            <liferay-ui:search-container-column-text cssClass="poppins-regular" name="Designation" property="designation" />
            <liferay-ui:search-container-column-text cssClass="poppins-regular" name="Phone" property="phoneNumber"/>
            <liferay-ui:search-container-column-text cssClass="poppins-regular" name="Email" property="emailAddress"/>
            <liferay-ui:search-container-column-text cssClass="poppins-regular" name="City" property="city"/>
            <liferay-ui:search-container-column-text cssClass="text-td-center" name="Actions">
                <c:if test="${not isSiteEmployee}">
                    <a href="${updateEmployeeRenderURL}" class="action-link"
                        ><img class="action-icon-img" src="${editEmployeeIcon}" alt="#"
                      /></a>

                    <a href="javascript:void(0);" class="action-link"
                        onclick="openDeletePopup('${deleteEmployeeActionURL}')">
                        <img class="action-icon-img"  src="${deleteEmployeeIcon}" alt="#"/>
                    </a>
                </c:if>

                <a href="${downloadEmployeePdfURL}" target="_blank" class="action-link">
                    <img
                      class="action-icon-img"
                      src="${pdfDownloadIcon}" alt="#"/>
                </a>
            </liferay-ui:search-container-column-text>
        </liferay-ui:search-container-row>

        <liferay-ui:search-iterator markupView="lexicon" />
    </liferay-ui:search-container>
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
<script src="${pageContext.request.contextPath}/js/deletePopup.js" type="text/javascript"></script>