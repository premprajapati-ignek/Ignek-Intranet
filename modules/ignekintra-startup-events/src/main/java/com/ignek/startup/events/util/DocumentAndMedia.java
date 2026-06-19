package com.ignek.startup.events.util;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FileUtil;

import java.io.InputStream;

public class DocumentAndMedia {
    private static final Log log = LogFactoryUtil.getLog(DocumentAndMedia.class);

    public static Folder createFolder(long groupId, long userId, String folderName, ServiceContext serviceContext){
        try {
            Folder folder = null;
            String description = "This is " + folderName + " folder";
            try{
                folder = DLAppLocalServiceUtil.getFolder(groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, folderName);
            } catch (Exception e) {
                log.info("Folder " + folderName + " not found.");
            }
            if (folder == null) {
                folder = DLAppLocalServiceUtil.addFolder(null, userId,
                        groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, folderName, description,
                        serviceContext);
                log.info("Folder created:  " + folderName);
            } else {
                log.info("Folder already exists: " + folderName);
            }
            return folder;
        } catch (Exception e) {
            log.error("Error while creating folder in document and media", e);
            return null;
        }
    }

    public static void uploadFile(long groupId, long userId, String fileNameWithExtension, String folderName, ServiceContext serviceContext) {
        try {
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));

            Folder folder;
            long folderId;
            try {
                folder = DLAppLocalServiceUtil.getFolder(groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, folderName);
                folderId = folder.getFolderId();
            } catch (Exception e) {
                log.error("'" + folderName + "' Folder not-found for " + fileName + " file upload");
                return;
            }

            FileEntry fileEntry = null;
            try {
                fileEntry = DLAppLocalServiceUtil.getFileEntry(groupId, folderId, fileName);
            } catch (Exception e) {
                log.info("File not found, will create new: " + fileName);
            }

            String resourcePath = "images/" + folderName + "/" + fileNameWithExtension;
            InputStream inputStream = DocumentAndMedia.class.getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                log.error("Resource not found: " + resourcePath);
                return;
            }
            byte[] bytes = FileUtil.getBytes(inputStream);
            String mimeType = fileNameWithExtension.endsWith(".png") ? StartupEventsPortletKeys.MIME_TYPE_PNG : StartupEventsPortletKeys.MIME_TYPE_JPG;

            if (fileEntry == null) {
                fileEntry = DLAppLocalServiceUtil.addFileEntry(null, userId, groupId,
                        folderId, fileName, mimeType, bytes, null, null,
                        null, serviceContext);
                log.info("FileEntry created");
            } else {
                log.info("File entry already exists.");
            }
        } catch (Exception e) {
            log.error("uploadFile error.", e);
        }
    }
}
