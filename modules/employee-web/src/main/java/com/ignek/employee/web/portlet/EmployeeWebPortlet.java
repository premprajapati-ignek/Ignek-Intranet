package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import com.liferay.portal.kernel.util.ParamUtil;
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

	@ProcessAction(name = "addEmployee")
	public void addEmployee(ActionRequest actionRequest, ActionResponse actionResponse){
		long employeeId = counterLocalService.increment(Employee.class.getName());
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		String designation = ParamUtil.getString(actionRequest, "designation");
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String phoneNumber = ParamUtil.getString(actionRequest, "phoneNumber");
		String addressLine1 = ParamUtil.getString(actionRequest, "addressLine1");
		String addressLine2 = ParamUtil.getString(actionRequest, "addressLine2");
		String city = ParamUtil.getString(actionRequest, "city");
		String zipCode = ParamUtil.getString(actionRequest, "zipCode");

		Employee employee = employeeLocalService.createEmployee(employeeId);
		employee.setEmployeeId(employeeId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setDesignation(designation);
		employee.setEmailAddress(emailAddress);
		employee.setPhoneNumber(phoneNumber);
		employee.setAddressLine1(addressLine1);
		employee.setAddressLine2(addressLine2);
		employee.setCity(city);
		employee.setZipCode(zipCode);

		employeeLocalService.addEmployee(employee);
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		List<Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		renderRequest.setAttribute("employeeList", employeeList);
		super.render(renderRequest, renderResponse);
	}

	@Reference
	CounterLocalService counterLocalService;
	@Reference
	EmployeeLocalService employeeLocalService;
}