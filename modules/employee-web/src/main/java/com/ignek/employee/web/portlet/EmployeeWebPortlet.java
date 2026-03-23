package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.*;

import com.liferay.portal.kernel.util.ParamUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.ByteArrayOutputStream;
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

	@ProcessAction(name = "addUpdateEmployee")
	public void addUpdateEmployee(ActionRequest actionRequest, ActionResponse actionResponse){
		long employeeId = ParamUtil.getLong(actionRequest, "employeeId");
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		String designation = ParamUtil.getString(actionRequest, "designation");
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String phoneNumber = ParamUtil.getString(actionRequest, "phoneNumber");
		String addressLine1 = ParamUtil.getString(actionRequest, "addressLine1");
		String addressLine2 = ParamUtil.getString(actionRequest, "addressLine2");
		String city = ParamUtil.getString(actionRequest, "city");
		String zipCode = ParamUtil.getString(actionRequest, "zipCode");

		Employee employee = null;

		if (employeeId > 0){
			try{
				employee = employeeLocalService.getEmployee(employeeId);
			} catch (Exception e){
				log.error(e.getCause(), e);
			}
		} else {
			employeeId = counterLocalService.increment(Employee.class.getName());
			employee = employeeLocalService.createEmployee(employeeId);
		}

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

		if (employeeId > 0 && employee.isNew()){
			employeeLocalService.addEmployee(employee);
		} else {
			employeeLocalService.updateEmployee(employee);
		}
	}

	@ProcessAction(name = "deleteEmployee")
	public void deleteEmployee(ActionRequest actionRequest, ActionResponse actionResponse){
		long employeeId = ParamUtil.getLong(actionRequest, "employeeId");
		try{
			employeeLocalService.deleteEmployee(employeeId);
		} catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		List<Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		renderRequest.setAttribute("employeeList", employeeList);
		super.render(renderRequest, renderResponse);
	}


	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws IOException, PortletException {

		if (!"downloadEmployeePdf".equals(ParamUtil.getString(request, "action"))) {
			return;
		}

		try {
			long employeeId = ParamUtil.getLong(request, "employeeId");
			Employee employee = employeeLocalService.getEmployee(employeeId);

			if (employee == null) {
				response.getWriter().write("Employee not found");
				return;
			}

			String content = "Employee Id : " + employee.getEmployeeId() + "\n" +
				"First Name : " + employee.getFirstName() + "\n" +
				"Last Name : " + employee.getLastName() + "\n" +
				"Email : " + employee.getEmailAddress() + "\n" +
				"Phone Number : " + employee.getPhoneNumber() + "\n" +
				"City : " + employee.getCity();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document, out);
			document.open();

			Font font = new Font(Font.HELVETICA, 12);

			for (String line : content.split("\n")) {
				document.add(new Paragraph(line, font));
			}

			document.close();

			PortletResponseUtil.sendFile(
					request,
					response,
					"Employee_" + employeeId + ".pdf",
					out.toByteArray(),
					"application/pdf"
			);

		} catch (Exception e) {
			log.error("PDF error", e);
			response.getWriter().write("Error generating PDF");
		}
	}

	@Reference
	CounterLocalService counterLocalService;
	@Reference
	EmployeeLocalService employeeLocalService;
}