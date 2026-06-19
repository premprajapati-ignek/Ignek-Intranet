package com.ignek.startup.events.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SiteRole {
    private final static Log log = LogFactoryUtil.getLog(SiteRole.class);

    public static void createSiteRole(long companyId, long userId, String roleName, ServiceContext serviceContext){
        try {
            Role role = RoleLocalServiceUtil.fetchRole(companyId, roleName);

            if (Validator.isNull(role)) {
                Map<Locale, String> titleMap = new HashMap<>();
                Map<Locale,String> descriptionMap = new HashMap<>();
                titleMap.put(Locale.US, roleName);
                descriptionMap.put(Locale.US, "This is site-level role");

                RoleLocalServiceUtil.addRole(
                        null,
                        userId,
                        Role.class.getName(),
                        0,
                        roleName,
                        titleMap,
                        descriptionMap,
                        RoleConstants.TYPE_SITE,
                        null,
                        serviceContext
                );
                log.info("Site-role successfully created...");
            } else {
                log.info("Site-role '" + roleName + "' already exists");
            }
        } catch (Exception e) {
            log.error("Error creating site-role '" + roleName + "'", e);
        }
    }
}
