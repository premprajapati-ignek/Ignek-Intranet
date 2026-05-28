package com.ignek.email.scheduler.util;

import com.ignek.email.scheduler.configuration.EmailSchedulerJobConfiguration;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfConverter {
    private static final Log log = LogFactoryUtil.getLog(EmailSchedulerJobConfiguration.class);

    public static byte[] convertArticleToPDF(List<JournalArticle> journalArticles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            if (journalArticles != null && journalArticles.isEmpty()) {
                document.add(new Paragraph("No journal articles found."));
            } else {
                for(JournalArticle article : journalArticles) {
                    String title = article.getTitle("en_US");

                    Font titleFont = FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,14,Font.BOLD);
                    Paragraph titleParagraph = new Paragraph(title, titleFont);
                    titleParagraph.setSpacingAfter(10f);
                    document.add(titleParagraph);

                    String content = article.getContent();
                    String cleanContent = content
                            .replaceAll("(?i)<br\\s*/?>", "\n")
                            .replaceAll("(?i)</p>", "\n")
                            .replaceAll("(?i)</div>", "\n")
                            .replaceAll("<[^>]*>", "")
                            .replaceAll("\\]\\]>", "")
                            .replaceAll("&nbsp;", " ")
                            .replaceAll("[ \\t]+", " ")
                            .replaceAll("\\n{2,}", "\n\n")
                            .trim();
                    Paragraph contentParagraph = new Paragraph(cleanContent);
                    contentParagraph.setSpacingAfter(20f);
                    document.add(contentParagraph);
                }
            }

            document.close();
        } catch (Exception e) {
            log.error("Error while converting journal articles to PDF", e);
        }

        return baos.toByteArray();
    }
}
