package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.*;
<<<<<<< Updated upstream
import com.liferay.portal.kernel.service.UserLocalService;
=======
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
>>>>>>> Stashed changes
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.io.IOException;
import java.util.List;

@Component(
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=EmployeeWeb",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + EmployeeWebPortletKeys.EMPLOYEEWEB,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)

public class EmployeeWebPortlet extends MVCPortlet {
	private Log log = LogFactoryUtil.getLog(this.getClass().getName());

<<<<<<< Updated upstream
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		try {
			List<Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			renderRequest.setAttribute(EmployeeWebPortletKeys.EMPLOYEE_LIST, employeeList);
			super.render(renderRequest, renderResponse);
			log.info("Employees retrieved successfully...");
=======
	@Reference
	protected IndexerRegistry indexerRegistry;
	@Reference
	protected EmployeeLocalService employeeLocalService;
	@Reference
	protected UserGroupRoleLocalService userGroupRoleLocalService;
	@Reference
	protected RoleLocalService roleLocalService;

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay =
				(ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User currentUser = themeDisplay.getUser();
		boolean isSiteEmployee = false;
		try {
			long userId = currentUser.getUserId();
			long groupId = themeDisplay.getScopeGroupId();
			String roleNameToCheck = EmployeeWebPortletKeys.SITE_EMPLOYEE;
			List<UserGroupRole> userGroupRoles = userGroupRoleLocalService.getUserGroupRoles(userId, groupId);

			for (UserGroupRole userGroupRole : userGroupRoles){
				Role role = roleLocalService.getRole(userGroupRole.getRoleId());
				if (roleNameToCheck.equals(role.getName())){
					isSiteEmployee = true;
					break;
				}
			}

			Indexer<Employee> indexer =
					indexerRegistry.getIndexer(Employee.class);

			SearchContext searchContext = new SearchContext();
			searchContext.setCompanyId(themeDisplay.getCompanyId());
			searchContext.setStart(QueryUtil.ALL_POS);
			searchContext.setEnd(QueryUtil.ALL_POS);

			String keywords = ParamUtil.getString(renderRequest, "keywords");
			searchContext.setKeywords(keywords);

			Hits hits = indexer.search(searchContext);

			List<Employee> employeeList = new ArrayList<>();

			for (Document doc : hits.getDocs()) {
				long employeeId = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

				Employee employee = employeeLocalService.getEmployee(employeeId);
				employee.setFirstName(doc.get(EmployeeWebPortletKeys.FIRST_NAME));
				employee.setLastName(doc.get(EmployeeWebPortletKeys.LAST_NAME));
				employee.setEmailAddress(doc.get(EmployeeWebPortletKeys.EMAIL_ADDRESS));
				employee.setDesignation(doc.get(EmployeeWebPortletKeys.DESIGNATION));
				employee.setPhoneNumber(doc.get(EmployeeWebPortletKeys.PHONE_NUMBER));
				employee.setAddressLine1(doc.get(EmployeeWebPortletKeys.ADDRESS_LINE_1));
				employee.setAddressLine2(doc.get(EmployeeWebPortletKeys.ADDRESS_LINE_2));
				employee.setCity(doc.get(EmployeeWebPortletKeys.CITY));
				employee.setZipCode(doc.get(EmployeeWebPortletKeys.ZIP_CODE));

				employeeList.add(employee);
			}

			renderRequest.setAttribute(EmployeeWebPortletKeys.EMPLOYEE_LIST, employeeList);
			renderRequest.setAttribute(EmployeeWebPortletKeys.IS_SITE_EMPLOYEE, isSiteEmployee);

			log.info("Employees retrieved from Elasticsearch...");

>>>>>>> Stashed changes
		} catch (Exception e) {
			log.error("Error to found employees.", e);
		}
	}

	@Reference
	EmployeeLocalService employeeLocalService;
}