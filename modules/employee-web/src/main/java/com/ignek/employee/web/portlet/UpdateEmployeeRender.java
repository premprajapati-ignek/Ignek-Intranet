package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
        property = {
                "javax.portlet.name=" + EmployeeWebPortletKeys.EMPLOYEEWEB,
                "mvc.command.name=" + EmployeeWebPortletKeys.UPDATE_EMPLOYEE
        },
        service = MVCRenderCommand.class
)

public class UpdateEmployeeRender implements MVCRenderCommand {
    private static final Log log = LogFactoryUtil.getLog(UpdateEmployeeRender.class);

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        long employeeId = ParamUtil.getLong(renderRequest, EmployeeWebPortletKeys.EMPLOYEE_ID);
        try{
            Employee employee = null;
            if (employeeId > 0) {
                employee = employeeLocalService.getEmployee(employeeId);
            }
            renderRequest.setAttribute(EmployeeWebPortletKeys.EMPLOYEE, employee);
            log.info("Employee rendered in the form");
        } catch(Exception e) {
            log.error("Employee not found for the provided employeeId");
        }
        return "/add-update-employee.jsp";
    }
    @Reference
    EmployeeLocalService employeeLocalService;
}
