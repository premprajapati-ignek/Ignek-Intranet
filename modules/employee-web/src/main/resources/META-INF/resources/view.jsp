<%@ include file="/init.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ignek.employee.model.Employee" %>

<% List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList"); %>
<portlet:renderURL var="addEmployeeRenderURL">
    <portlet:param name="mvcPath" value="/add-employee.jsp"/>
</portlet:renderURL>

<div class="employees-container">
  <div class="container mt-4">
    <div class="d-flex justify-content-between">
      <p class="employee-title poppins-bold">Employees</p>
      <a
        class="add-employee-btn poppins-medium d-flex align-items-center justify-content-center"
        href="<%= addEmployeeRenderURL %>"
        >ADD NEW EMPLOYEE</a
      >
    </div>
    <hr />
    <table class="w-100">
      <tr class="poppins-semibold">
        <th>Sr No</th>
        <th>Name</th>
        <th>Designation</th>
        <th>Phone</th>
        <th>Email</th>
        <th>City</th>
        <th class="action-th">Actions</th>
      </tr>
      <c:forEach items="${employeeList}" var="employee">
        <tr class="poppins-regular">
            <td>${employee.employeeId}</td>
            <td>${employee.firstName}</td>
            <td>${employee.designation}</td>
            <td>${employee.phoneNumber}</td>
            <td>${employee.emailAddress}</td>
            <td>${employee.city}</td>
            <td class="action-td">
              <a href="" class="action-link"
                ><img class="action-icon-img" src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/images/edit.png") %>" alt="#"
              /></a>

              <a href="" class="action-link"
                ><img class="action-icon-img"  src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/images/delete.png") %>" alt="#"
              /></a>

              <a href="" class="action-link"
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