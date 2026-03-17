<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />
<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">

    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
    <aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

    <aui:fieldset>
        <aui:select label="Title" name="title" value="<%= title %>">
            <aui:option value="Employees">Employees</aui:option>
            <aui:option value="Technologies">Technologies</aui:option>
            <aui:option value="Images">Images</aui:option>
            <aui:option value="Users">Users</aui:option>
        </aui:select>
    </aui:fieldset>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>

</aui:form>