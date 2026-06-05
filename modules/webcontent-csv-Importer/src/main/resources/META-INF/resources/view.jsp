<%@ include file="/init.jsp" %>

<portlet:actionURL name="/upload/csv" var="uploadCSVURL" />

<aui:form action="<%= uploadCSVURL %>"
          enctype="multipart/form-data"
          method="post">

    <aui:input
        type="file"
        name="csvFile"
        label="Upload CSV" />

    <aui:button type="submit" value="Upload"/>
</aui:form>
