package com.ignek.employee.web.portlet;

import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalService;
import com.ignek.employee.web.constants.EmployeeWebPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.ByteArrayOutputStream;

@Component(
        property = {
                "javax.portlet.name=" + EmployeeWebPortletKeys.EMPLOYEEWEB,
                "mvc.command.name=" + EmployeeWebPortletKeys.DOWNLOAD_EMPLOYEE_PDF
        },
        service = MVCResourceCommand.class
)

public class EmployeePdfExporter implements MVCResourceCommand{
    private Log log = LogFactoryUtil.getLog(this.getClass().getName());

    @Override
    public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws PortletException {
        try {
            long employeeId = ParamUtil.getLong(resourceRequest, EmployeeWebPortletKeys.EMPLOYEE_ID);
            Employee employee = employeeLocalService.getEmployee(employeeId);

            if (employee == null) {
                log.error("Employee not found");
                return true;
            }

            String content = "Employee Details : " + "\n" + " \n" +
                    "Employee Id : " + employee.getEmployeeId() + "\n" +
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

            Font font = new Font(Font.TIMES_ROMAN, 12);

            for (String line : content.split("\n")) {
                document.add(new Paragraph(line, font));
            }

            document.close();

            PortletResponseUtil.sendFile(
                    resourceRequest,
                    resourceResponse,
                    "Employee_" + employeeId + ".pdf",
                    out.toByteArray(),
                    "application/pdf"
            );
            log.info("PDF Generated");
            return false;
        }
        catch (Exception e) {
            log.error("Error generating PDF", e);
            return true;
        }
    }
    @Reference
    EmployeeLocalService employeeLocalService;
}