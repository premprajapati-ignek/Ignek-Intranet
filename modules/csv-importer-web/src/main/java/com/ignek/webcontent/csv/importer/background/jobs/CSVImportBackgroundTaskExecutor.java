package com.ignek.webcontent.csv.importer.background.jobs;

import com.ignek.webcontent.csv.importer.constants.WebcontentCsvImporterPortletKeys;
import com.ignek.webcontent.csv.importer.service.HeadlessWebContentService;
import com.liferay.portal.kernel.backgroundtask.*;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Component(
    property = {
            "background.task.executor.class.name=" +
                    WebcontentCsvImporterPortletKeys.BACKGROUND_TASK_CLASS_NAME
    },
    service = BackgroundTaskExecutor.class
)

public class CSVImportBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {
    private static final Log log = LogFactoryUtil.getLog(CSVImportBackgroundTaskExecutor.class);

    public CSVImportBackgroundTaskExecutor() {
        setBackgroundTaskStatusMessageTranslator(
            new BackgroundTaskStatusMessageTranslator() {
                @Override
                public void translate(BackgroundTaskStatus backgroundTaskStatus, Message message) {
                }
            });
    }

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {

        log.info("CSV Import Background Task Started");

        Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
        String csvFilePath = (String) taskContextMap.get(WebcontentCsvImporterPortletKeys.CSV_FILE_PATH);
        log.info("csvFilePath: " + csvFilePath);

        JSONArray batchPayload = JSONFactoryUtil.createJSONArray();
        int totalRows = 0;

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : parser) {
                totalRows++;

                String storeName = record.get(WebcontentCsvImporterPortletKeys.STORE_NAME);
                String storeAddress = record.get(WebcontentCsvImporterPortletKeys.STORE_ADDRESS);

                JSONObject webContent = JSONFactoryUtil.createJSONObject();

                webContent.put(WebcontentCsvImporterPortletKeys.CONTENT_STRUCTURE_ID, WebcontentCsvImporterPortletKeys.CONTENT_STRUCTURE_ID_VALUE);
                webContent.put(WebcontentCsvImporterPortletKeys.TITLE, storeName);
                webContent.put(WebcontentCsvImporterPortletKeys.VIEWABLE_BY, WebcontentCsvImporterPortletKeys.VIEWABLE_BY_VALUE);

                JSONArray contentFields = JSONFactoryUtil.createJSONArray();

                contentFields.put(JSONFactoryUtil.createJSONObject()
                        .put(WebcontentCsvImporterPortletKeys.NAME, WebcontentCsvImporterPortletKeys.STORE_NAME)
                        .put(WebcontentCsvImporterPortletKeys.CONTENT_FIELD_VALUE, JSONFactoryUtil.createJSONObject()
                                .put(WebcontentCsvImporterPortletKeys.DATA, storeName)));

                contentFields.put(JSONFactoryUtil.createJSONObject()
                        .put(WebcontentCsvImporterPortletKeys.NAME, WebcontentCsvImporterPortletKeys.STORE_ADDRESS)
                        .put(WebcontentCsvImporterPortletKeys.CONTENT_FIELD_VALUE, JSONFactoryUtil.createJSONObject()
                                .put(WebcontentCsvImporterPortletKeys.DATA, storeAddress)));

                webContent.put(WebcontentCsvImporterPortletKeys.CONTENT_FIELDS, contentFields);
                batchPayload.put(webContent);

                log.info("Prepared Row: " + storeName);
            }

            log.info("Total Rows: " + totalRows);

            headlessWebContentService.createWebContent(batchPayload);

            Files.deleteIfExists(Paths.get(csvFilePath));
            log.info("CSV deleted");
            log.info("Background Task Completed");

            return BackgroundTaskResult.SUCCESS;

        } catch (Exception e) {

            log.error("CSV Processing Failed: ", e);
            return new BackgroundTaskResult(BackgroundTaskConstants.STATUS_FAILED, e.getMessage());

        }
    }

    @Override
    public BackgroundTaskExecutor clone() {
        return this;
    }

    @Override
    public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
        return null;
    }

    @Reference
    HeadlessWebContentService headlessWebContentService;
}
