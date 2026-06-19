<%@ include file="/init.jsp" %>

<portlet:actionURL name="/upload/csv" var="uploadCSVURL" />

<div class="csv-upload-container">
    <aui:form action="<%= uploadCSVURL %>"
              enctype="multipart/form-data"
              method="post">

        <aui:input
            type="file"
            name="csvFile"
            label="Upload Articles CSV"
            labelCssClass="poppins-regular"
            cssClass="poppins-regular"
        >
            <aui:validator name="required" errorMessage="Please, select the file." />
        </aui:input>

        <div class="d-flex flex-row-reverse">
            <aui:button cssClass="upload-btn poppins-semibold" type="submit" value="Upload"/>
        </div>
    </aui:form>
</div>