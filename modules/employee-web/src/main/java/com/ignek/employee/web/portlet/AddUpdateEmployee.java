package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Component(
        property = {
                "javax.portlet.name=" + EmployeeWebPortletKeys.EMPLOYEEWEB,
                "mvc.command.name=" + EmployeeWebPortletKeys.ADD_UPDATE_EMPLOYEE
        },
        service = MVCActionCommand.class
)

public class AddUpdateEmployee extends BaseMVCActionCommand {
    private Log log = LogFactoryUtil.getLog(this.getClass().getName());

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
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
                }
                catch (Exception e){
                    log.error("Employee not found", e);
                }
            }
            else {
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

                Role siteEmployeeRole = roleLocalService.getRole(companyId, EmployeeWebPortletKeys.SITE_EMPLOYEE);
                userGroupRoleLocalService.addUserGroupRoles(user.getUserId(), groupId, new long[]{siteEmployeeRole.getRoleId()});

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
                log.info("Employee created successfully");
            }
            else {
                employeeLocalService.updateEmployee(employee);
                log.info("Employee updated successfully");
            }

            Indexer<Employee> indexer = IndexerRegistryUtil.nullSafeGetIndexer(Employee.class);
            indexer.reindex(employee);
        } catch (Exception e) {
            log.error("Failed to create/update employee", e);
        }
    }
    @Reference
    EmployeeLocalService employeeLocalService;
    @Reference
    CounterLocalService counterLocalService;
    @Reference
    UserLocalService userLocalService;
    @Reference
    RoleLocalService roleLocalService;
    @Reference
    UserGroupRoleLocalService userGroupRoleLocalService;
}