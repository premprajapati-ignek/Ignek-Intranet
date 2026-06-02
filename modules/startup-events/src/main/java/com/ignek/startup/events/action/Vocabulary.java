package com.ignek.startup.events.action;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Vocabulary {
    private static final Log log = LogFactoryUtil.getLog(Category.class);

    public static AssetVocabulary createVocabulary(long groupId, long userId, ServiceContext serviceContext, String vocabularyName) {
        if (Validator.isNull(vocabularyName) || groupId <= 0) {
            log.error("Vocabulary name or groupId is invalid.");
            return null;
        }

        AssetVocabulary vocabulary = null;
        try {
            List<AssetVocabulary> vocabularies = AssetVocabularyLocalServiceUtil.getGroupVocabularies(groupId, false);
            vocabulary = vocabularies.stream()
                    .filter(assetVocabulary ->  assetVocabulary.getName().equalsIgnoreCase(vocabularyName))
                    .findFirst()
                    .orElse(null);

            if (vocabulary != null) {
                log.info("Vocabulary already exists: " + vocabularyName);
                return vocabulary;
            }
        } catch (Exception e) {
            log.info("Vocabulary not found, creating new");
        }

        if (Validator.isNull(vocabulary)) {
            try {
                Map<Locale, String> titleMap = new HashMap<>();
                Map<Locale, String> descriptionMap = new HashMap<>();
                titleMap.put(LocaleUtil.getDefault(), vocabularyName);

                vocabulary = AssetVocabularyLocalServiceUtil.addVocabulary(
                        userId, groupId, null, titleMap,
                        descriptionMap, null, serviceContext);

                log.info("Vocabulary successfully created.");
                return vocabulary;
            }
            catch (Exception e) {
                log.error("Error creating vocabulary: " + vocabularyName, e);
            }
        }
        return null;
    }
}
