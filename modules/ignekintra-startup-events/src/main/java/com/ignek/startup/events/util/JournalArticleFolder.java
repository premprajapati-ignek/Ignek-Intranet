package com.ignek.startup.events.util;

import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

public class JournalArticleFolder {
    private static final Log log = LogFactoryUtil.getLog(JournalArticleFolder.class);

    public static JournalFolder createJournalFolderInSite(long groupId, long userId, ServiceContext serviceContext, String folderName) {
        long parentFolderId = 0;
        String name = folderName;
        String description = "This is " + folderName + " Folder.";
        JournalFolder folder = null;
        try {
            folder = JournalFolderLocalServiceUtil.fetchFolder(groupId, parentFolderId, name);
            if (folder == null) {
                folder = JournalFolderLocalServiceUtil.addFolder(
                        null, userId,groupId,
                        parentFolderId, name, description, serviceContext);
                log.info("Journal folder created: " + folder.getName());
            } else {
                log.info("Journal folder already exists: " + folder.getName());
            }
        } catch (Exception e) {
            log.error("Error while creating Journal Folder: ", e);
        }
        return folder;
    }
}
