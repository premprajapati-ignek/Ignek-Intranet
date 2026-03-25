package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.*;
import com.liferay.portal.kernel.service.UserLocalService;
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

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		try {
			List<Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			renderRequest.setAttribute(EmployeeWebPortletKeys.EMPLOYEE_LIST, employeeList);
			super.render(renderRequest, renderResponse);
			log.info("Employees retrieved successfully...");
		} catch (Exception e) {
			log.error("Error to found employees.", e);
		}
	}

	@Reference
	EmployeeLocalService employeeLocalService;
}