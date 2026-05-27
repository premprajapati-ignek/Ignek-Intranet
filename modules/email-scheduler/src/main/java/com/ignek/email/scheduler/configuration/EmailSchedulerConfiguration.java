package com.ignek.email.scheduler.configuration;

import aQute.bnd.annotation.metatype.Meta;
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
            description = "Send an email notification to all employees with articles in pdf attachment",
            name = "Email Scheduler",
            required = false
    )
    public int interval();

    @Meta.AD(
            deflt = "Employee-News",
            description = "Sets the categorization of the article",
            name = "Article Category",
            required = false
    )
    public String categoryName();
}