package com.ignek.startup.events.action;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Category {
    private static final Log log = LogFactoryUtil.getLog(Category.class);

    public static void createCategory(long groupId, long userId, ServiceContext serviceContext, long vocabularyId, String vocabularyName, String categoryName) {
        try {
            AssetCategory category = AssetCategoryLocalServiceUtil.fetchCategory(
                    groupId, 0, categoryName, vocabularyId);

            if (category != null) {
                log.info("Category '" + categoryName + "' already exists in vocabulary: " + vocabularyName);
                return;
            }

            log.info("Category not found, creating new: " + categoryName);

            Map<Locale, String> categoryTitleMap = new HashMap<>();
            Map<Locale, String> categoryDescriptionMap = new HashMap<>();
            categoryTitleMap.put(Locale.US, categoryName);
            categoryDescriptionMap.put(Locale.US, categoryName);

            AssetCategoryLocalServiceUtil.addCategory(
                    null, userId, groupId,
                    0, categoryTitleMap, categoryDescriptionMap,
                    vocabularyId, null, serviceContext);
            log.info("Category successfully created.");
        } catch (Exception e) {
            log.error("Error creating category '" + categoryName + "' in site " + groupId, e);
        }
    }
}
