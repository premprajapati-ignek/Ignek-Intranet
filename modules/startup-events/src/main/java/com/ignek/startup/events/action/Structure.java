package com.ignek.startup.events.action;

import com.ignek.startup.events.constants.StartupEventsPortletKeys;
import com.liferay.dynamic.data.mapping.constants.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Structure {
    private static final Log log = LogFactoryUtil.getLog(Structure.class);

    public static DDMStructure createCardStructure(
            long groupId,
            long userId,
            DDMStructureLocalService structureLocalService,
            ServiceContext serviceContext,
            long journalArticleClassNameId) {

        DDMStructure cardStructure = null;
        try {
            String structureKey = StartupEventsPortletKeys.CARD;
            String content = new ReadFileFromPath().getFile(StartupEventsPortletKeys.STRUCTURE_PATH);
            DDMForm ddmForm = DDMUtil.getDDMForm(content);

            if (ddmForm == null || ddmForm.getDDMFormFields().isEmpty()) {
                log.error("DDMForm has no fields. Check structure JSON file.");
                return null;
            }

            log.info("DDMForm has " + ddmForm.getDDMFormFields().size() + " fields.");


            try {
                cardStructure = structureLocalService.getStructure(
                        groupId, journalArticleClassNameId, structureKey);
            } catch (Exception e) {
                log.info("Structure not found, creating new: " + structureKey);
            }

            if (cardStructure != null) {
                cardStructure.setDDMForm(ddmForm);
                cardStructure = structureLocalService.updateStructure(
                        userId,
                        cardStructure.getStructureId(),
                        ddmForm,
                        DDMUtil.getDefaultDDMFormLayout(ddmForm),
                        serviceContext
                );
                log.info("Structure updated: " + structureKey);
            } else {
                Map<Locale, String> nameMap = new HashMap<>();
                nameMap.put(Locale.US, structureKey);
                Map<Locale, String> descriptionMap = new HashMap<>();
                descriptionMap.put(Locale.US, structureKey);

                cardStructure = structureLocalService.addStructure(
                        userId, groupId,
                        StringPool.BLANK,
                        journalArticleClassNameId,
                        structureKey,
                        nameMap,
                        descriptionMap,
                        ddmForm,
                        DDMUtil.getDefaultDDMFormLayout(ddmForm),
                        StorageType.JSON.toString(),
                        DDMStructureConstants.TYPE_DEFAULT,
                        serviceContext
                );
                log.info("Structure created: " + structureKey);
            }
        } catch (Exception e) {
            log.error("Error creating/updating structure: ", e);
        }
        return cardStructure;
    }
}
