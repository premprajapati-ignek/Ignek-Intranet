package com.ignek.email.scheduler.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.ignek.email.scheduler.constants.EmailSchedulerPortletKeys;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(category = "infrastructure")
@Meta.OCD(
        id = "com.ignek.email.scheduler.configuration.EmailSchedulerConfiguration",
        localization = "content/Language",
        name = "Employee Article Email Notification"
)

public interface EmailSchedulerConfiguration {
    @Meta.AD(
            deflt = "1",
            description = EmailSchedulerPortletKeys.SCHEDULER_DESCRIPTION,
            name = EmailSchedulerPortletKeys.EMAIL_SCHEDULER,
            required = false
    )
    public int interval();

    @Meta.AD(
            deflt = "Employee-News",
            description = EmailSchedulerPortletKeys.ARTICLE_DESCRIPTION,
            name = EmailSchedulerPortletKeys.ARTICLE_CATEGORY,
            required = false
    )
    public String categoryName();
}