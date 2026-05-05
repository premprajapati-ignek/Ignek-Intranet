package com.ignek.startup.events.action;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class JournalArticleWebContent {
    private static final Log log = LogFactoryUtil.getLog(JournalArticleWebContent.class);

    public static void createWebContentInSite(long groupId, long userId, ServiceContext serviceContext, String articleId, String filePath, long structureId, long folderId) throws Exception {
        try{
            String content = new ReadFileFromPath().getFile(filePath);

            Map<Locale, String> titleMap = new LinkedHashMap<>();
            Map<Locale, String> descriptionMap = new LinkedHashMap<>();
            titleMap.put(Locale.US, articleId);
            descriptionMap.put(Locale.US, content);

            JournalArticle journalArticle = null;

            try {
                journalArticle = JournalArticleLocalServiceUtil.fetchLatestArticleByExternalReferenceCode(groupId, articleId);
            } catch (Exception e) {
                log.error("JournalArticle not found, creating new: " + articleId);
            }

            if (journalArticle != null) {
                JournalArticleLocalServiceUtil.updateArticle(userId, groupId, folderId,
                        journalArticle.getArticleId(), journalArticle.getVersion(), titleMap, descriptionMap,
                        content, null, serviceContext);
                log.info("JournalArticle updated: " + articleId);
            } else {
                String ddmTemplateKey = StartupEventsPortletKeys.CARD;
                serviceContext.setWorkflowAction(com.liferay.portal.kernel.workflow.WorkflowConstants.ACTION_PUBLISH);

                JournalArticleLocalServiceUtil.addArticle(
                        articleId, userId, groupId,
                        folderId, titleMap, descriptionMap,
                        content, structureId, ddmTemplateKey,
                        serviceContext
                );
                log.info("JournalArticle created: " + articleId);
            }
        } catch (Exception e) {
            log.error("Exception occurred while trying to create webcontent.", e);
        }
    }
}
