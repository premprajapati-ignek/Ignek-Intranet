package com.ignek.employee.search;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.search.BaseIndexer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.List;
import java.util.Locale;

@Component(
    immediate = true,
    property = {
            "indexer.class.name=com.ignek.employee.model.Employee"
    },service = Indexer.class
)

public class EmployeeIndexer extends BaseIndexer<Employee> {

    public static final String CLASS_NAME = Employee.class.getName();

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    protected void doDelete(Employee employee) throws Exception {
        deleteDocument(employee.getCompanyId(), employee.getEmployeeId());
    }

    @Override
    protected Document doGetDocument(Employee employee) throws Exception {

        Document document = getBaseModelDocument(CLASS_NAME, employee);

        document.addKeyword("firstName", employee.getFirstName());
        document.addKeyword("lastName", employee.getLastName());
        document.addText("emailAddress", employee.getEmailAddress());
        document.addText("designation", employee.getDesignation());
        document.addKeyword("phoneNumber", employee.getPhoneNumber());
        document.addText("addressLine1", employee.getAddressLine1());
        document.addText("addressLine2", employee.getAddressLine2());
        document.addText("city", employee.getCity());
        document.addKeyword("zipCode", employee.getZipCode());

        document.addKeyword(Field.COMPANY_ID, employee.getCompanyId());
        document.addKeyword(Field.GROUP_ID, employee.getGroupId());

        return document;
    }

    @Override
    protected Summary doGetSummary(
            Document document, Locale locale, String snippet,
            PortletRequest portletRequest,
            PortletResponse portletResponse) {

        String title = document.get("firstName") + " " + document.get("lastName");
        String content = document.get("emailAddress");

        return createSummary(document, title, content);
    }

    @Override
    protected void doReindex(Employee employee) throws Exception {
        Document document = doGetDocument(employee);
        indexWriterHelper.updateDocument(employee.getCompanyId(), document);
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {
        Employee employee = employeeLocalService.getEmployee(classPK);
        doReindex(employee);
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {
        long companyId = Long.parseLong(ids[0]);
        int start = -1;
        int end = -1;

        List<Employee> employees =
                employeeLocalService.getEmployees(start, end);
        if (!employees.isEmpty()){
            for (Employee employee : employees) {
                doReindex(employee);
            }
        }
    }

    @Reference
    protected EmployeeLocalService employeeLocalService;

    @Reference
    protected IndexWriterHelper indexWriterHelper;
}
