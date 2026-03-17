<%@ include file="/init.jsp" %>

<portlet:actionURL name="updateEmployee" var="updateEmployeeActionURL"/>
<aui:form action="<%=updateEmployeeActionURL%>" name="employeeForm"/>

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

<aui:form action="<%=updateEmployeeActionURL%>" method="post">
    <aui:input name="employeeId" type="hidden" value="<%=Long.parseLong(employeeId)%>"/>
    <aui:input name="firstName" type="text" value="${employee.firstName}"/>
    <aui:input name="lastName" type="text" value="${employee.lastName}"/>
    <aui:input name="firstName" type="text" value="${employee.firstName}"/>
</aui:form>
