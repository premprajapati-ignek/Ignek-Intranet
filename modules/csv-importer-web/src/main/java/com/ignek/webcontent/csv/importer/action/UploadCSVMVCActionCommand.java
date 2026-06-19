package com.ignek.webcontent.csv.importer.action;

import com.ignek.webcontent.csv.importer.constants.WebcontentCsvImporterPortletKeys;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
            "javax.portlet.name=" + WebcontentCsvImporterPortletKeys.ARTICLES_CSV_WEB,
            "mvc.command.name=/upload/csv"
        },
        service = MVCActionCommand.class
)

public class UploadCSVMVCActionCommand extends BaseMVCActionCommand {
    private static final Log log = LogFactoryUtil.getLog(UploadCSVMVCActionCommand.class);

    @Override
    protected void doProcessAction( ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        File file = uploadPortletRequest.getFile(WebcontentCsvImporterPortletKeys.CSV_FILE_NAME);
        String fileName = uploadPortletRequest.getFileName(WebcontentCsvImporterPortletKeys.CSV_FILE_NAME);

        if (Validator.isNull(file) || !file.exists()) {
            log.error("Upload failed. CSV file is missing or empty.");
            return;
        }

        try {
            log.info("CSV Upload Started");
            log.info("Received CSV file: " + fileName);
            Path tempFile = Files.createTempFile("csv-upload-", ".csv");

            Files.copy(
                    file.toPath(),
                    tempFile,
                    StandardCopyOption.REPLACE_EXISTING);

            String csvFilePath = tempFile.toString();
            log.info("CSV saved at: " + csvFilePath);

            Map<String, Serializable> taskContextMap = new HashMap<>();
            taskContextMap.put(WebcontentCsvImporterPortletKeys.CSV_FILE_PATH, csvFilePath);
            taskContextMap.put(WebcontentCsvImporterPortletKeys.COMPANY_ID, PortalUtil.getCompanyId(actionRequest));
            long userId = PortalUtil.getUserId(actionRequest);

            BackgroundTask backgroundTask = backgroundTaskLocalService
                    .addBackgroundTask(
                        userId,
                        PortalUtil.getScopeGroupId(actionRequest),
                        WebcontentCsvImporterPortletKeys.BACKGROUND_TASK_NAME,
                        WebcontentCsvImporterPortletKeys.BACKGROUND_TASK_CLASS_NAME,
                        taskContextMap,
                        new ServiceContext());

            log.info("Background task created.");
            log.info("Background task Id: " + backgroundTask.getBackgroundTaskId());
        } catch (Exception exception) {
            log.error("Error while processing CSV upload", exception);
        }
    }

    @Reference
    private BackgroundTaskLocalService backgroundTaskLocalService;
}