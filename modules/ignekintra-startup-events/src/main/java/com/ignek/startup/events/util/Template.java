package com.ignek.startup.events.util;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.liferay.portal.kernel.service.ClassNameLocalServiceUtil.getClassNameId;

public class Template {
    private static final Log log = LogFactoryUtil.getLog(Template.class);

    public static void createCardTemplate(
            long groupId,
            long userId,
            DDMTemplateLocalService templateLocalService,
            ServiceContext serviceContext,
            long journalArticleClassNameId) {

        try {
            String templateKey = StartupEventsPortletKeys.CARD;
            String script = new ReadFileFromPath().getFile(StartupEventsPortletKeys.TEMPLATE_PATH);
            long structureClassNameId = getClassNameId(DDMStructure.class.getName());

            DDMStructure ddmStructure = DDMStructureLocalServiceUtil.getStructure(
                    groupId, journalArticleClassNameId, StartupEventsPortletKeys.CARD);
            long structureId = ddmStructure.getStructureId();

            DDMTemplate cardTemplate = null;
            try {
                cardTemplate = templateLocalService.getTemplate(
                        groupId, structureClassNameId, templateKey);
            } catch (Exception e) {
                log.info("No template found, creating new: " + templateKey);
            }

            if (Validator.isNotNull(cardTemplate)) {
                cardTemplate.setScript(script);
                templateLocalService.updateDDMTemplate(cardTemplate);
                log.info("Template updated: " + templateKey);
            } else {
                Map<Locale, String> nameMap = new HashMap<>();
                nameMap.put(Locale.US, templateKey);
                Map<Locale, String> descriptionMap = new HashMap<>();
                descriptionMap.put(Locale.US, templateKey);

                templateLocalService.addTemplate(
                        null,
                        userId, groupId,
                        structureClassNameId,
                        structureId,
                        journalArticleClassNameId,
                        templateKey,
                        nameMap,
                        descriptionMap,
                        DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
                        DDMTemplateConstants.TEMPLATE_MODE_CREATE,
                        StartupEventsPortletKeys.LANG_FTL,
                        script,
                        true,
                        false,
                        null,
                        null,
                        serviceContext
                );
                log.info("Template created: " + templateKey);
            }
        } catch (Exception e) {
            log.error("Error creating/updating template: ", e);
        }
    }
}
