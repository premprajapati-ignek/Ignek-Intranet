package com.ignek.startup.events.action;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.journal.model.JournalFolder;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.util.*;
import org.osgi.framework.*;
import org.osgi.service.component.annotations.Component;
import java.util.*;

@Component(
        immediate = true,
        property = {
                "key=application.startup.events"
        },
        service = BundleActivator.class
)
public class ConfigurationCreator implements BundleActivator {

    private static final Log log = LogFactoryUtil.getLog(ConfigurationCreator.class);

    @Override
    public void start(BundleContext context) throws Exception {

        ServiceReference<DDMStructureLocalService> structureRef =
                context.getServiceReference(DDMStructureLocalService.class);
        DDMStructureLocalService structureLocalService = context.getService(structureRef);
        ServiceReference<DDMTemplateLocalService> templateRef =
                context.getServiceReference(DDMTemplateLocalService.class);
        DDMTemplateLocalService templateLocalService = context.getService(templateRef);

        long companyId = CompanyLocalServiceUtil
                .getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID))
                .getCompanyId();

        Group group = GroupLocalServiceUtil.getGroup(
                companyId, StartupEventsPortletKeys.IGNEK_INTRANET);
        long groupId = group.getGroupId();
        long userId = group.getCreatorUserId();

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setCompanyId(companyId);
        serviceContext.setUserId(userId);
        serviceContext.setAddGroupPermissions(true);
        serviceContext.setAddGuestPermissions(false);

        long journalArticleClassNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class.getName());
        //site-DDMStructure
        DDMStructure structure = Structure.createCardStructure(groupId, userId, structureLocalService, serviceContext, journalArticleClassNameId);
        long structureId = structure.getStructureId();
        //site-DDMTemplate
        Template.createCardTemplate(groupId, userId, templateLocalService, serviceContext, journalArticleClassNameId);

        //site widget template
        long portletDisplayTemplateClassNameId =
                PortalUtil.getClassNameId(StartupEventsPortletKeys.PORTLET_DISPLAY_TEMPLATE_CLASS);

        Map<String, String> adtMap = new LinkedHashMap<>();
        adtMap.put("ADT/cardListAssetPublisher.ftl", StartupEventsPortletKeys.PORTLET_ASSET_PUBLISHER);
        adtMap.put("ADT/breadcrumb.ftl", StartupEventsPortletKeys.PORTLET_BREADCRUMB);
        adtMap.put("ADT/searchBar.ftl", StartupEventsPortletKeys.PORTLET_SEARCH_BAR);
        adtMap.put("ADT/themeSearchBar.ftl", StartupEventsPortletKeys.PORTLET_SEARCH_BAR);
        adtMap.put("ADT/cardSearchResult.ftl", StartupEventsPortletKeys.PORTLET_SEARCH_RESULT);
        adtMap.put("ADT/categoryFacet.ftl", StartupEventsPortletKeys.PORTLET_CATEGORY_FACET);
        adtMap.put("ADT/categoryFilter.ftl", StartupEventsPortletKeys.PORTLET_CATEGORY_FILTER);
        adtMap.put("ADT/mediaGallery.ftl", StartupEventsPortletKeys.PORTLET_MEDIA_GALLERY);

        for (Map.Entry<String, String> entry : adtMap.entrySet()) {
            String filePath = entry.getKey();
            long classPK = PortalUtil.getClassNameId(entry.getValue());
            String templateKey = filePath
                    .substring(filePath.lastIndexOf("/") + 1)
                    .replace(".ftl", "")
                    .toUpperCase();

            ADTTemplate.createADTTemplate(groupId, userId, templateLocalService, serviceContext,
                    portletDisplayTemplateClassNameId, classPK, filePath, templateKey);
        }

        //Site Vocabulary
        String vocabularyName = StartupEventsPortletKeys.TECHNOLOGIES;
        AssetVocabulary vocabulary = Vocabulary.createVocabulary(groupId, userId, serviceContext, vocabularyName);
        long vocabularyId = vocabulary.getVocabularyId();

        //Site Categories
        List<String> categoryList = new ArrayList<>();
        categoryList.add(StartupEventsPortletKeys.CAT_LIFERAY);
        categoryList.add(StartupEventsPortletKeys.CAT_JAVA);
        categoryList.add(StartupEventsPortletKeys.CAT_REACT);

        for (String categoryName : categoryList){
            Category.createCategory(groupId, userId, serviceContext, vocabularyId, vocabularyName, categoryName);
        }

        //Site-Role
        List<String> siteRoleList = new ArrayList<>();
        siteRoleList.add(StartupEventsPortletKeys.SITE_HR);
        siteRoleList.add(StartupEventsPortletKeys.SITE_EMPLOYEE);
        for (String siteRoleName : siteRoleList) {
            SiteRole.createSiteRole(companyId, userId, siteRoleName, serviceContext);
        }

        //Site-Layout
        Map<String, String> sitePageLayoutList = new LinkedHashMap<>();
        sitePageLayoutList.put(StartupEventsPortletKeys.DASHBOARD, StartupEventsPortletKeys.DASHBOARD_URL);
        sitePageLayoutList.put(StartupEventsPortletKeys.EMPLOYEE, StartupEventsPortletKeys.EMPLOYEE_URL);
        sitePageLayoutList.put(StartupEventsPortletKeys.HR, StartupEventsPortletKeys.HR_URL);
        sitePageLayoutList.put(StartupEventsPortletKeys.SETTINGS, StartupEventsPortletKeys.SETTINGS_URL);
        sitePageLayoutList.put(StartupEventsPortletKeys.SEARCH, StartupEventsPortletKeys.SEARCH_URL);
        for (Map.Entry<String, String> entry : sitePageLayoutList.entrySet()) {
            String sitePageLayoutName = entry.getKey();
            String sitePageLayoutUrl = entry.getValue();
            Layout.createSiteLayout(groupId, userId, serviceContext, sitePageLayoutName, sitePageLayoutUrl);
        }

        //Site-journalFolder
        String webContentFolderName = StartupEventsPortletKeys.TECHNOLOGIES;
        JournalFolder journalFolder = JournalArticleFolder.createJournalFolderInSite(groupId, userId, serviceContext, webContentFolderName);

        //Site-webcontent
        Map<String, String> journalArticleList = new LinkedHashMap<>();
        journalArticleList.put(StartupEventsPortletKeys.JAVA_01, "webContent/Java_1.xml");
        journalArticleList.put(StartupEventsPortletKeys.JAVA_02, "webContent/Java_2.xml");
        journalArticleList.put(StartupEventsPortletKeys.LIFERAY_01, "webContent/Liferay_1.xml");
        journalArticleList.put(StartupEventsPortletKeys.LIFERAY_02, "webContent/Liferay_2.xml");
        journalArticleList.put(StartupEventsPortletKeys.REACT_01, "webContent/React_1.xml");
        journalArticleList.put(StartupEventsPortletKeys.REACT_02, "webContent/React_2.xml");
        for (Map.Entry<String, String> entry : journalArticleList.entrySet()) {
            String articleId = entry.getKey();
            String filePath = entry.getValue();
            JournalArticleWebContent.createWebContentInSite(groupId, userId, serviceContext, articleId, filePath, structureId, journalFolder.getFolderId());
        }

        //site-FragmentCollection
        String fragmentSetName = StartupEventsPortletKeys.IGNEK_INTRANET;
        FragmentCollection fragmentCollection = Fragment.createFragmentCollectionInSite(groupId, userId, fragmentSetName, serviceContext);

        //Site-FragmentEntry
        List<String> fragmentEntryKeyList = new ArrayList<>();
        fragmentEntryKeyList.add(StartupEventsPortletKeys.BANNER);
        fragmentEntryKeyList.add(StartupEventsPortletKeys.GLOBAL_CSS);
        for (String fragmentEntryKey :  fragmentEntryKeyList ) {
            Fragment.createFragmentEntryInSite(groupId, userId, fragmentCollection.getFragmentCollectionId(), fragmentEntryKey, serviceContext);
        }

        //Site-Folder
        List<String> folderNameList = new ArrayList<>();
        folderNameList.add(StartupEventsPortletKeys.FOLDER_ARTICLE_ICON);
        folderNameList.add(StartupEventsPortletKeys.FOLDER_MEDIA);
        folderNameList.add(StartupEventsPortletKeys.FOLDER_BANNER);
        for(String folderName : folderNameList){
            Folder folder = DocumentAndMedia.createFolder(groupId, userId, folderName, serviceContext);
        }

        //Upload images
        Map<String, String> uploadFileList = new LinkedHashMap<>();
        uploadFileList.put("java.png", StartupEventsPortletKeys.FOLDER_ARTICLE_ICON);
        uploadFileList.put("liferay.png", StartupEventsPortletKeys.FOLDER_ARTICLE_ICON);
        uploadFileList.put("react.png", StartupEventsPortletKeys.FOLDER_ARTICLE_ICON);
        uploadFileList.put("dashboardBanner.png", StartupEventsPortletKeys.FOLDER_BANNER);
        for (int i=1; i<=6; i++) {
            uploadFileList.put("corporate" + i + ".jpg", StartupEventsPortletKeys.FOLDER_MEDIA);
        }
        for(Map.Entry<String, String> entry : uploadFileList.entrySet()){
            String fileNameWithExtension = entry.getKey();
            String folderName  = entry.getValue();
            DocumentAndMedia.uploadFile(groupId, userId, fileNameWithExtension, folderName, serviceContext);
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        log.info("ConfigurationCreator stopped.");
    }
}