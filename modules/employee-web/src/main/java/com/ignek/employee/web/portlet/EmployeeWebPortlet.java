package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.*;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

	@ProcessAction(name = EmployeeWebPortletKeys.ADD_UPDATE_EMPLOYEE)
	public void addUpdateEmployee(ActionRequest actionRequest, ActionResponse actionResponse){
		try{
			long employeeId = ParamUtil.getLong(actionRequest, EmployeeWebPortletKeys.EMPLOYEE_ID);
			String firstName = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.FIRST_NAME);
			String lastName = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.LAST_NAME);
			String designation = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.DESIGNATION);
			String emailAddress = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.EMAIL_ADDRESS);
			String phoneNumber = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.PHONE_NUMBER);
			String addressLine1 = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.ADDRESS_LINE_1);
			String addressLine2 = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.ADDRESS_LINE_2);
			String city = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.CITY);
			String zipCode = ParamUtil.getString(actionRequest, EmployeeWebPortletKeys.ZIP_CODE);

			Employee employee = null;

			if (employeeId > 0){
				try{
					employee = employeeLocalService.getEmployee(employeeId);

					User user = userLocalService.getUser(employee.getUserId());
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setEmailAddress(emailAddress);
					user.setJobTitle(designation);
					user.setScreenName(firstName+lastName);
					userLocalService.updateUser(user);
				} catch (Exception e){
					log.error(e.getCause(), e);
				}
			} else {
				ServiceContext serviceContext = ServiceContextFactory.getInstance(Employee.class.getName(), actionRequest);
				long creatorUserId = serviceContext.getUserId();
				long companyId = serviceContext.getCompanyId();
				long groupId = serviceContext.getScopeGroupId();

				boolean autoScreenName = true;
				boolean autoPassword = false;
				boolean male = true;
				boolean sendMail = false;
				String password1 = EmployeeWebPortletKeys.PASSWORD1;
				String password2 = EmployeeWebPortletKeys.PASSWORD2;
				String screenName = firstName + lastName;
				String middleName = EmployeeWebPortletKeys.MIDDLE_NAME;
				LocalDate birthDate = LocalDate.of(EmployeeWebPortletKeys.BIRTH_YEAR, EmployeeWebPortletKeys.BIRTH_MONTH, EmployeeWebPortletKeys.BIRTH_DAY);
				int birthYear = birthDate.getYear();
				int birthMonth = birthDate.getMonthValue();
				int birthDay = birthDate.getDayOfMonth();
				int type = UserConstants.TYPE_REGULAR;
				Locale locale= Locale.getDefault();

				long groupIds[] = new long[10];
				groupIds[0] = groupId;

				User user = userLocalService.addUser(creatorUserId, companyId, autoPassword, password1, password2,
						autoScreenName, screenName, emailAddress, locale, firstName, middleName, lastName, -1, -1, male,
						birthMonth, birthDay, birthYear, designation, type, groupIds, null, null, null, sendMail,
						serviceContext);

				employeeId = counterLocalService.increment(Employee.class.getName());
				employee = employeeLocalService.createEmployee(employeeId);
				employee.setUserId(user.getUserId());
				employee.setGroupId(groupId);
				employee.setCompanyId(companyId);
				employee.setCreateDate(new Date());
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
			employee.setModifiedDate(new Date());

			if (employeeId > 0 && employee.isNew()){
				employeeLocalService.addEmployee(employee);
			} else {
				employeeLocalService.updateEmployee(employee);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@ProcessAction(name = EmployeeWebPortletKeys.DELETE_EMPLOYEE)
	public void deleteEmployee(ActionRequest actionRequest, ActionResponse actionResponse){
		long employeeId = ParamUtil.getLong(actionRequest, EmployeeWebPortletKeys.EMPLOYEE_ID);
		try{
			Employee employee = employeeLocalService.getEmployee(employeeId);
			userLocalService.deleteUser(employee.getUserId());
			employeeLocalService.deleteEmployee(employeeId);
		} catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		List<Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		renderRequest.setAttribute(EmployeeWebPortletKeys.EMPLOYEE_LIST, employeeList);
		super.render(renderRequest, renderResponse);
	}


	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws IOException, PortletException {

		if (!EmployeeWebPortletKeys.DOWNLOAD_EMPLOYEE_PDF.equals(ParamUtil.getString(request, EmployeeWebPortletKeys.ACTION))) {
			return;
		}

		try {
			long employeeId = ParamUtil.getLong(request, EmployeeWebPortletKeys.EMPLOYEE_ID);
			Employee employee = employeeLocalService.getEmployee(employeeId);

			if (employee == null) {
				response.getWriter().write("Employee not found");
				return;
			}

			String content = "Employee Id : " + employee.getEmployeeId() + "\n" +
				"User Id : " + employee.getUserId() + "\n" +
				"Name : " + employee.getFirstName() + " " + employee.getLastName() + "\n" +
				"Email : " + employee.getEmailAddress() + "\n" +
				"Designation : " + employee.getDesignation() + "\n" +
				"Phone Number : " + employee.getPhoneNumber() + "\n" +
				"Address : " + employee.getAddressLine1() + ", " + employee.getAddressLine2() + "\n" +
				"City : " + employee.getCity() + "\n" +
				"Post Code / Zip Code : " + employee.getZipCode();

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
	UserLocalService userLocalService;
	@Reference
	CounterLocalService counterLocalService;
	@Reference
	EmployeeLocalService employeeLocalService;
}