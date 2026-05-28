package com.ignek.email.scheduler.configuration;

import com.ignek.email.scheduler.constants.EmailSchedulerPortletKeys;
import com.ignek.email.scheduler.util.EmailTemplate;
import com.ignek.email.scheduler.util.PdfConverter;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.*;
import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.scheduler.SchedulerJobConfiguration;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.TriggerConfiguration;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.lowagie.text.*;
import org.osgi.service.component.annotations.*;

import javax.mail.internet.InternetAddress;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        configurationPid = "com.ignek.email.scheduler.configuration.EmailSchedulerConfiguration",
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        service = SchedulerJobConfiguration.class
)

public class EmailSchedulerJobConfiguration implements SchedulerJobConfiguration {

    private static final Log log = LogFactoryUtil.getLog(EmailSchedulerJobConfiguration.class);

    @Override
    public UnsafeRunnable<Exception> getJobExecutorUnsafeRunnable() {
        return () -> {
            try {
                long companyId = PortalUtil.getDefaultCompanyId();

                String siteName = EmailSchedulerPortletKeys.IGNEK_INTRANET;
                String vocabularyName = EmailSchedulerPortletKeys.ANNOUNCEMENT;
                String categoryName = emailSchedulerConfiguration.categoryName();
                Group ignekSite = groupLocalService.getGroup(companyId, siteName);

                if (ignekSite != null) {
                    long groupId = ignekSite.getGroupId();
                    System.out.println("Found Site GroupId : " + groupId);
                    sendEmailToEmployee(companyId, groupId, vocabularyName, categoryName);
                }
            } catch (Exception e) {
                log.error("Error while fetching the groupId : " + e);
            }
        };
    }

    @Override
    public TriggerConfiguration getTriggerConfiguration() {
        return TriggerConfiguration.createTriggerConfiguration(
                emailSchedulerConfiguration.interval(),
                TimeUnit.WEEK);
    }

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {

        emailSchedulerConfiguration = ConfigurableUtil.createConfigurable(
                                        EmailSchedulerConfiguration.class,
                                        properties);
        log.info("Scheduler Interval: " + emailSchedulerConfiguration.interval());
        log.info("Article Category: " + emailSchedulerConfiguration.categoryName());
    }

    private void sendEmailToEmployee(long companyId, long groupId, String vocabularyName, String categoryName) {
        try {
            Role siteEmployeeRole = roleLocalService.getRole(companyId, EmailSchedulerPortletKeys.SITE_EMPLOYEE);
            List<UserGroupRole> userGroupRoles = userGroupRoleLocalService
                    .getUserGroupRolesByGroupAndRole(groupId, siteEmployeeRole.getRoleId());

            if (userGroupRoles.isEmpty()) {
                log.info("No users found with 'Site Employee' role for groupId: " + groupId);
                return;
            }

            List<JournalArticle> journalArticles = getArticlesContentByCategory(
                    groupId, vocabularyName, categoryName);
            if (journalArticles.isEmpty()) {
                log.info("No journal articles found for groupId : " + groupId);
                return;
            }

            byte[] pdfBytes = PdfConverter.convertArticleToPDF(journalArticles);
            File tempFile = File.createTempFile(EmailSchedulerPortletKeys.EMPLOYEE_ARTICLES, ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(pdfBytes);
            } catch (IOException e) {
                log.error("Error writing PDF bytes to file: ", e);
            }

            String body = EmailTemplate.getEmailTemplate();
            InternetAddress fromAddress = new InternetAddress(EmailSchedulerPortletKeys.FROM_EMAIL);

            for (UserGroupRole userGroupRole : userGroupRoles) {
                try {
                    User user = userLocalService.getUser(userGroupRole.getUserId());

                    if (!user.isActive()) {
                        continue;
                    }

                    MailMessage mailMessage = new MailMessage();

                    mailMessage.setFrom(fromAddress);
                    mailMessage.setTo(new InternetAddress(user.getEmailAddress()));

                    mailMessage.setSubject(EmailSchedulerPortletKeys.EMAIL_SUBJECT);

                    mailMessage.setBody(body);
                    mailMessage.setHTMLFormat(true);
                    mailMessage.addFileAttachment(tempFile, EmailSchedulerPortletKeys.EMPLOYEE_ARTICLES + ".pdf");

                    mailService.sendEmail(mailMessage);
                    log.info("Email Notification sent to: " + user.getEmailAddress());
                } catch (Exception e) {
                    log.error("Failed to send email to userId: " + userGroupRole.getUserId(), e);
                }
            }
        } catch (Exception e) {
            log.error("Email sending error", e);
        }
    }

    private List<JournalArticle> getArticlesContentByCategory(long groupId, String vocabularyName, String categoryName) {
        List<JournalArticle> journalArticles = new ArrayList<>();
        try {
            List<AssetVocabulary> vocabularies = assetVocabularyLocalService
                    .getGroupVocabularies(groupId, false);

            AssetVocabulary vocabulary = vocabularies.stream()
                    .filter(v -> v.getName().equalsIgnoreCase(vocabularyName))
                    .findFirst()
                    .orElse(null);

            if (vocabulary == null) {
                log.info("vocabulary not found for groupId : " + groupId);
                return journalArticles;
            }

            long vocabularyId = vocabulary.getVocabularyId();
            AssetCategory category = assetCategoryLocalService.fetchCategory(groupId, 0, categoryName, vocabularyId);

            if (category == null) {
                log.info("category not found for groupId : " + groupId);
                return journalArticles;
            }

            long categoryId = category.getCategoryId();
            long classNameId = PortalUtil.getClassNameId(JournalArticle.class.getName());

            AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
            assetEntryQuery.setClassNameIds(new long[]{classNameId});
            assetEntryQuery.setAllCategoryIds(new long[]{categoryId});
            assetEntryQuery.setGroupIds(new long[]{groupId});

            List<AssetEntry> assetEntries = assetEntryLocalService.getEntries(assetEntryQuery);

            for (AssetEntry assetEntry : assetEntries) {
                try {
                    JournalArticle article = journalArticleLocalService.getLatestArticle(assetEntry.getClassPK());

                    if (article != null) {
                        journalArticles.add(article);
                    }
                } catch (Exception e) {
                    log.error("Could not fetch article for classPK: " + assetEntry.getClassPK(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error while getting articles content", e);
        }

        return journalArticles;
    }

    private volatile EmailSchedulerConfiguration emailSchedulerConfiguration;
    @Reference
    private MailService mailService;
    @Reference
    private AssetCategoryLocalService assetCategoryLocalService;
    @Reference
    private AssetEntryLocalService assetEntryLocalService;
    @Reference
    private AssetVocabularyLocalService assetVocabularyLocalService;
    @Reference
    private JournalArticleLocalService journalArticleLocalService;
    @Reference
    private GroupLocalService groupLocalService;
    @Reference
    private RoleLocalService roleLocalService;
    @Reference
    private UserGroupRoleLocalService userGroupRoleLocalService;
    @Reference
    private UserLocalService userLocalService;
}