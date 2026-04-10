package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
        property = {
                "javax.portlet.name=" + EmployeeWebPortletKeys.EMPLOYEEWEB,
                "mvc.command.name=" + EmployeeWebPortletKeys.DELETE_EMPLOYEE
        },
        service = MVCActionCommand.class
)

public class DeleteEmployee extends BaseMVCActionCommand {
    private Log log = LogFactoryUtil.getLog(this.getClass().getName());

    @Reference
    EmployeeLocalService employeeLocalService;
    @Reference
    UserLocalService userLocalService;

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        long employeeId = ParamUtil.getLong(actionRequest, EmployeeWebPortletKeys.EMPLOYEE_ID);
        try{
            Employee employee = employeeLocalService.getEmployee(employeeId);
            userLocalService.deleteUser(employee.getUserId());
            employeeLocalService.deleteEmployee(employeeId);

            Indexer<Employee> indexer = IndexerRegistryUtil.nullSafeGetIndexer(Employee.class);
            indexer.delete(employee);
            log.info("Employee removed from the elasticsearch document");
            log.info("Employee deleted successfully");
        } catch (Exception e){
            log.error("Failed to delete employee", e);
        }
    }
}
