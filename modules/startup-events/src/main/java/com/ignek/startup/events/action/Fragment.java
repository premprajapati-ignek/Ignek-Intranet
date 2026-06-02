package com.ignek.startup.events.action;

import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentCollectionLocalServiceUtil;
import com.liferay.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

public class Fragment {
    private static final Log log = LogFactoryUtil.getLog(Fragment.class);

    public static FragmentCollection createFragmentCollectionInSite(long groupId, long userId, String fragmentSetName, ServiceContext serviceContext) {
        FragmentCollection fragmentCollection = null;
        try {
            fragmentCollection = FragmentCollectionLocalServiceUtil.fetchFragmentCollection(groupId, fragmentSetName);
            if (fragmentCollection == null) {
                String ERC = fragmentSetName;
                String fragmentCollectionKey = fragmentSetName;
                String description = "This is " + fragmentSetName + " fragment set";

                fragmentCollection = FragmentCollectionLocalServiceUtil.addFragmentCollection(ERC, userId, groupId,
                        fragmentCollectionKey, fragmentSetName, description, serviceContext);
                log.info("FragmentCollection created: " + fragmentSetName);
            } else {
                log.info("FragmentCollection already exists: " + fragmentSetName);
            }
            return fragmentCollection;
        } catch (Exception e) {
            log.error("Error creating fragment collection in site", e);
            return null;
        }
    }

    public static void createFragmentEntryInSite(long groupId, long userId, long fragmentCollectionId, String fragmentEntryKey, ServiceContext serviceContext) {
        String name = fragmentEntryKey.toUpperCase();
        FragmentEntry fragmentEntry = null;

        try {
            String html = new ReadFileFromPath().getFile("/fragments/" + fragmentEntryKey + "/index.html");
            String css = new ReadFileFromPath().getFile("/fragments/" + fragmentEntryKey + "/index.css");
            String js = new ReadFileFromPath().getFile("/fragments/" + fragmentEntryKey + "/index.js");
            String configuration = new ReadFileFromPath().getFile("/fragments/" + fragmentEntryKey + "/configuration.json");

            fragmentEntry = FragmentEntryLocalServiceUtil.fetchFragmentEntry(groupId, fragmentEntryKey);

            if (fragmentEntry == null) {
                log.info("FragmentEntry not found, creating new: " + name);

                FragmentEntryLocalServiceUtil.addFragmentEntry(fragmentEntryKey, userId, groupId, fragmentCollectionId,
                        fragmentEntryKey, name, css, html, js, true, configuration, null, 0,
                        false, 0, "", 0, serviceContext);
                log.info("Fragment entry created: " + name);
            } else {
                FragmentEntryLocalServiceUtil.updateFragmentEntry(userId, fragmentEntry.getFragmentEntryId(),
                        fragmentCollectionId, name, css, html, js, true, configuration, "",
                        0, false, "", 0);
                log.info("Fragment entry updated: " + name);
            }
        } catch (Exception e) {
            log.error("Error while creating "+ name + " fragment entry ", e);
        }
    }
}
