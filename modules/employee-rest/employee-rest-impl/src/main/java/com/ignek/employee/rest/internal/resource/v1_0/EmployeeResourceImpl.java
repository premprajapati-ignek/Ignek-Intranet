package com.ignek.employee.rest.internal.resource.v1_0;

import com.ignek.employee.rest.dto.v1_0.Employee;
import com.ignek.employee.rest.resource.v1_0.EmployeeResource;
import com.ignek.employee.service.EmployeeLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.vulcan.pagination.Page;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/employee.properties",
	scope = ServiceScope.PROTOTYPE, service = EmployeeResource.class
)
public class EmployeeResourceImpl extends BaseEmployeeResourceImpl {

	private Log log = LogFactoryUtil.getLog(this.getClass().getName());
	@Reference
	private EmployeeLocalService employeeLocalService;
	@Reference
	private CounterLocalService counterLocalService;
	@Reference
	private UserLocalService userLocalService;

	@Override
	public Page<Employee> getEmployeesPage() throws Exception {
		List<Employee> dtoList = null;
		try{
			List<com.ignek.employee.model.Employee> employeeList = employeeLocalService.getEmployees(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			dtoList = employeeList.stream().map(employee -> this._toDTO(employee)).collect(Collectors.toList());
			log.info("Employee retrieved using rest-api");
		} catch (Exception e){
			log.error("Error fetching employees");
		}
		return Page.of(dtoList);
	}

	@Override
	public Employee postEmployee(Employee employee) throws Exception{
		long employeeId = counterLocalService.increment(com.ignek.employee.model.Employee.class.getName());
		com.ignek.employee.model.Employee currentEmployee = employeeLocalService.createEmployee(employeeId);
		try{
			currentEmployee.setFirstName(employee.getFirstName());
			currentEmployee.setLastName(employee.getLastName());
			currentEmployee.setDesignation(employee.getDesignation());
			currentEmployee.setEmailAddress(employee.getEmailAddress());
			currentEmployee.setPhoneNumber(employee.getPhoneNumber());
			currentEmployee.setAddressLine1(employee.getAddressLine1());
			currentEmployee.setAddressLine2(employee.getAddressLine2());
			currentEmployee.setCity(employee.getCity());
			currentEmployee.setZipCode(employee.getZipCode());

			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
			long creatorUserId = serviceContext.getUserId();
			long companyId = serviceContext.getCompanyId();
			long groupId = serviceContext.getScopeGroupId();

			boolean autoScreenName = true;
			boolean autoPassword = false;
			boolean male = true;
			boolean sendMail = false;

			String password1 = "test";
			String password2 = "test";
			String screenName = employee.getFirstName() + employee.getLastName();
			String middleName = "";
			LocalDate birthDate = LocalDate.of(2000, 06, 25);
			int birthYear = birthDate.getYear();
			int birthMonth = birthDate.getMonthValue();
			int birthDay = birthDate.getDayOfMonth();
			int type = UserConstants.TYPE_REGULAR;
			Locale locale = Locale.getDefault();

			long groupIds[] = new long[10];
			groupIds[0] = groupId;

			User user = userLocalService.addUser(creatorUserId, companyId, autoPassword, password1, password2, autoScreenName, screenName, employee.getEmailAddress(), locale, employee.getFirstName(), middleName, employee.getLastName(), -1, -1, male, birthMonth, birthDay, birthYear, employee.getDesignation(), type, groupIds, null, null, null, sendMail, serviceContext);
			currentEmployee.setUserId(user.getUserId());
			currentEmployee.setGroupId(groupId);
			currentEmployee.setCompanyId(companyId);
			currentEmployee.setCreateDate(new Date());

			currentEmployee = employeeLocalService.addEmployee(currentEmployee);
			log.info("Employee created successfully using rest-api");
		} catch (Exception e){
			log.error("Failed to create employee", e);
		}
		return _toDTO(currentEmployee);
	}

	@Override
	public Employee putEmployee(Integer employeeId, Employee employee) throws Exception {
		com.ignek.employee.model.Employee currentEmployee = employeeLocalService.getEmployee(employeeId);

		try {
			User user = userLocalService.getUser(currentEmployee.getUserId());
			user.setFirstName(employee.getFirstName());
			user.setLastName(employee.getLastName());
			user.setEmailAddress(employee.getEmailAddress());
			user.setJobTitle(employee.getDesignation());
			user.setScreenName(employee.getFirstName() + employee.getLastName());
			userLocalService.updateUser(user);

			currentEmployee.setFirstName(employee.getFirstName());
			currentEmployee.setLastName(employee.getLastName());
			currentEmployee.setDesignation(employee.getDesignation());
			currentEmployee.setEmailAddress(employee.getEmailAddress());
			currentEmployee.setPhoneNumber(employee.getPhoneNumber());
			currentEmployee.setAddressLine1(employee.getAddressLine1());
			currentEmployee.setAddressLine2(employee.getAddressLine2());
			currentEmployee.setCity(employee.getCity());
			currentEmployee.setZipCode(employee.getZipCode());
			currentEmployee.setModifiedDate(new Date());

			currentEmployee = employeeLocalService.updateEmployee(currentEmployee);
			log.info("Employee updated successfully using rest-api");
		} catch (Exception e){
			log.error("Failed to update employee", e);
		}
		return _toDTO(currentEmployee);
	}

	@Override
	public Employee getEmployee(Integer employeeId) throws Exception {
		com.ignek.employee.model.Employee employee = employeeLocalService.getEmployee(employeeId);
		return _toDTO(employee);
	}

	@Override
	public Employee deleteEmployee(Integer employeeId) throws Exception {
		com.ignek.employee.model.Employee employee = employeeLocalService.getEmployee(employeeId);
		try{
			userLocalService.deleteUser(employee.getUserId());
			employee = employeeLocalService.deleteEmployee(employeeId);
			log.info("Employee deleted successfully using rest-api");
		} catch(Exception e){
			log.error("Failed to delete employee");
		}
		return _toDTO(employee);
	}

	private Employee _toDTO(com.ignek.employee.model.Employee employee){
		return new Employee() {{
			setEmployeeId(Math.toIntExact(employee.getEmployeeId()));
			setFirstName(employee.getFirstName());
			setLastName(employee.getLastName());
			setDesignation(employee.getDesignation());
			setEmailAddress(employee.getEmailAddress());
			setPhoneNumber(employee.getPhoneNumber());
			setAddressLine1(employee.getAddressLine1());
			setAddressLine2(employee.getAddressLine2());
			setCity(employee.getCity());
			setZipCode(employee.getZipCode());
		}};
	}
}