package com.ignek.startup.events.action;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Layout {
    private static final Log log = LogFactoryUtil.getLog(Layout.class);

    public static void createSiteLayout(long groupId, long userId, ServiceContext serviceContext, String sitePageLayoutName, String sitePageLayoutUrl) {
        try{
            String friendlyURL = sitePageLayoutUrl;
            boolean privateLayout = false;

            com.liferay.portal.kernel.model.Layout pageLayout = null;
            try {
                pageLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, privateLayout, friendlyURL);
                log.info("Page already exists : " + friendlyURL);
            } catch (Exception e) {
                log.info("Layout not found, creating new: " + friendlyURL);
            }

            if (pageLayout == null) {
                String description = "This is " +  sitePageLayoutName + " page.";
                String type = LayoutConstants.TYPE_CONTENT;
                boolean hidden = false;
                long parentLayoutId = 0L;

                Map<Locale, String> nameMap = new LinkedHashMap<>();
                nameMap.put(Locale.US, sitePageLayoutName);

                Map<Locale, String> titleMap = new LinkedHashMap<>();
                titleMap.put(Locale.US, sitePageLayoutName);

                Map<Locale, String> descriptionMap = new LinkedHashMap<>();
                descriptionMap.put(Locale.US, description);

                Map<Locale, String> friendlyURLMap = new LinkedHashMap<>();
                friendlyURLMap.put(Locale.US, friendlyURL);

                serviceContext.setLanguageId(Locale.US.toString());

                LayoutLocalServiceUtil.addLayout(
                        null,
                        userId, groupId,
                        privateLayout, parentLayoutId,
                        nameMap, titleMap, descriptionMap,
                        null,null,
                        type,null,
                        hidden,false,
                        friendlyURLMap, serviceContext
                );
                log.info("Page created: " + friendlyURL);
            }
        } catch (Exception e) {
            log.error("Error creating layout: ", e);
        }
    }
}
