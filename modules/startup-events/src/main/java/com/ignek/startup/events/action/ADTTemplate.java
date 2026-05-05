package com.ignek.startup.events.action;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ADTTemplate {
    private static final Log log = LogFactoryUtil.getLog(ADTTemplate.class);
    public static void createADTTemplate(
            long groupId,
            long userId,
            DDMTemplateLocalService templateLocalService,
            ServiceContext serviceContext,
            long resourceClassNameId,
            long classPK,
            String filePath,
            String templateKey) {

        try {
            String script = new ReadFileFromPath().getFile(filePath);

            if (Validator.isNull(script)) {
                log.error("ADT script is empty for path: " + filePath + ". Skipping.");
                return;
            }

            DDMTemplate adtTemplate = null;
            try {
                adtTemplate = templateLocalService.getTemplate(
                        groupId,
                        classPK,
                        templateKey
                );
            } catch (Exception e) {
                log.info("ADT template not found, will create: " + templateKey);
            }

            if (Validator.isNotNull(adtTemplate)) {
                adtTemplate.setScript(script);
                templateLocalService.updateDDMTemplate(adtTemplate);
                log.info("ADT Template updated: " + templateKey);
            } else {
                Map<Locale, String> nameMap = new HashMap<>();
                Map<Locale, String> descriptionMap = new HashMap<>();
                nameMap.put(Locale.US, templateKey);
                descriptionMap.put(Locale.US, templateKey);

                templateLocalService.addTemplate(
                        null,
                        userId,
                        groupId,
                        classPK,
                        0L,
                        resourceClassNameId,
                        templateKey,
                        nameMap,
                        descriptionMap,
                        DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
                        DDMTemplateConstants.TEMPLATE_MODE_CREATE,
                        StartupEventsPortletKeys.LANG_FTL,
                        script,
                        false,
                        false,
                        null,
                        null,
                        serviceContext
                );
                log.info("ADT Template created: " + templateKey);
            }
        } catch (Exception e) {
            log.error("Error while creating/updating ADT template: " + templateKey, e);
        }
    }
}
