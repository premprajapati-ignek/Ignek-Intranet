package com.ignek.email.scheduler.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class EmailTemplate {
    private static final Log log = LogFactoryUtil.getLog(EmailTemplate.class);

    public static String getEmailTemplate() {
        try {

            InputStream inputStream = EmailTemplate.class.getClassLoader().getResourceAsStream(
                    "META-INF/resources/html/emailTemplate.html");

            if (inputStream == null) {
                log.error("Email template file not found");
                return "<h3>Email template not found</h3>";
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            log.error("Error while getting email template", e);
        }

        return "<h3>Unable to load email template</h3>";
    }
}
