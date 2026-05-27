package com.ignek.email.scheduler.configuration;

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
import com.liferay.portal.kernel.scheduler.SchedulerJobConfiguration;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.TriggerConfiguration;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import javax.mail.internet.InternetAddress;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(
        configurationPid = "com.ignek.email.scheduler.configuration.EmailSchedulerConfiguration",
        service = SchedulerJobConfiguration.class
)

public class EmailSchedulerJobConfiguration implements SchedulerJobConfiguration {

    private static final Log log = LogFactoryUtil.getLog(EmailSchedulerJobConfiguration.class);

    @Override
    public UnsafeRunnable<Exception> getJobExecutorUnsafeRunnable() {
        return () -> {
            System.out.println("Hello Ignek Viewers :)");
            try {
                long companyId = PortalUtil.getDefaultCompanyId();

                String siteName = "Ignek Intranet";
                String vocabularyName = "announcement";
                String categoryName = emailSchedulerConfiguration.categoryName();
                Group ignekSite = groupLocalService.getGroup(companyId, siteName);

                if (ignekSite != null) {
                    long groupId = ignekSite.getGroupId();
                    System.out.println("Found Site GroupId : " + groupId);
                    sendEmail(groupId, vocabularyName, categoryName);
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
                TimeUnit.MINUTE);
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

    private void sendEmail(long groupId, String vocabularyName, String categoryName) {
        try {
            MailMessage mailMessage = new MailMessage();

            mailMessage.setFrom(new InternetAddress("prem.prajapati.ignek@gmail.com"));
            mailMessage.setTo(new InternetAddress("prajapatiprem724@gmail.com"));

            mailMessage.setSubject("Employee Notification");

            String articleContent = getArticlesContentByCategory(groupId, vocabularyName, categoryName);
            mailMessage.setBody(articleContent);

            mailMessage.setHTMLFormat(true);

            mailService.sendEmail(mailMessage);
            log.info("Employee notification email sent successfully");
        }
        catch (Exception e) {
            log.error("Email sending error", e);
        }
    }

    private String getArticlesContentByCategory(long groupId, String vocabularyName, String categoryName) {
        StringBuilder emailBody = new StringBuilder();

        try {

            List<AssetVocabulary> vocabularies = assetVocabularyLocalService.getGroupVocabularies(groupId, false);

            AssetVocabulary vocabulary = vocabularies.stream()
                    .filter(v -> v.getName().equalsIgnoreCase(vocabularyName))
                    .findFirst()
                    .orElse(null);

            if (vocabulary == null) {
                return "<p>No vocabulary found.</p>";
            }

            long vocabularyId = vocabulary.getVocabularyId();
            AssetCategory category = assetCategoryLocalService.fetchCategory(groupId, 0, categoryName, vocabularyId);

            if (category == null) {
                return "<p>No category found.</p>";
            }

            long categoryId = category.getCategoryId();
            long classNameId = PortalUtil.getClassNameId(JournalArticle.class.getName());

            AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
            assetEntryQuery.setClassNameIds(new long[]{classNameId});
            assetEntryQuery.setAllCategoryIds(new long[]{categoryId});
            assetEntryQuery.setGroupIds(new long[]{groupId});

            List<AssetEntry> assetEntries = assetEntryLocalService.getEntries(assetEntryQuery);
            System.out.println("AssetEntries : " + assetEntries);

            List<JournalArticle> journalArticles = new ArrayList<>();

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

            System.out.println("Articles : " + journalArticles);
            for (JournalArticle article : journalArticles) {
                emailBody.append(article.getTitle("en_US")).append("\n");
                emailBody.append(article.getContent()).append("\n");
            }
        } catch (Exception e) {
            log.error("Error while getting articles content", e);
        }

        return emailBody.toString();
    }

    private byte[] convertArticleToPDF(List<JournalArticle> journalArticles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            if (journalArticles != null && journalArticles.isEmpty()) {
                document.add(new Paragraph("No journal articles found."));
            } else {
                for(JournalArticle article : journalArticles) {
                    String title = article.getTitle("en_US");
                    Paragraph titleParagraph = new Paragraph(title);
                    titleParagraph.setSpacingAfter(10f);
                    document.add(titleParagraph);
                }
            }
        } catch (Exception e) {
            log.error("Error while converting journal articles to PDF", e);
        }
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
}