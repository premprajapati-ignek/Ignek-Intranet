package com.ignek.entity.count.portlet;

import com.ignek.entity.count.constants.EntityCountWebPortletKeys;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.*;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.io.IOException;

@Component(
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=EntityCountWeb",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + EntityCountWebPortletKeys.ENTITYCOUNTWEB,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class EntityCountWebPortlet extends MVCPortlet {

    private static final Log _log = LogFactoryUtil.getLog(EntityCountWebPortlet.class);

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            PortletPreferences portletPreferences = renderRequest.getPreferences();
            String title = portletPreferences.getValue(EntityCountWebPortletKeys.TITLE, EntityCountWebPortletKeys.EMPLOYEES);
            long countOfRecords = 0;
            long companyId = PortalUtil.getCompanyId(renderRequest);
            long groupId = PortalUtil.getScopeGroupId(renderRequest);

            if (title.equalsIgnoreCase(EntityCountWebPortletKeys.EMPLOYEES)) {
                Role siteEmployeeRole = roleLocalService.getRole(companyId, EntityCountWebPortletKeys.SITE_EMPLOYEE);
                countOfRecords = userGroupRoleLocalService.getUserGroupRolesByGroupAndRole(groupId, siteEmployeeRole.getRoleId()).size();
			} else if (title.equalsIgnoreCase(EntityCountWebPortletKeys.TECHNOLOGIES)) {
                countOfRecords = assetCategoryLocalService.getAssetCategoriesCount();
            } else if (title.equalsIgnoreCase(EntityCountWebPortletKeys.IMAGES)) {
                countOfRecords = dlFileEntryLocalService.getDLFileEntriesCount();
            } else if (title.equalsIgnoreCase(EntityCountWebPortletKeys.USERS)) {
                countOfRecords = userLocalService.getUsersCount();
            }
            renderRequest.setAttribute(EntityCountWebPortletKeys.COUNT, countOfRecords);
        } catch (Exception e) {
            _log.error("Error : " + e);
        }
        super.render(renderRequest, renderResponse);
    }
    @Reference
    private UserLocalService userLocalService;
    @Reference
    private DLFileEntryLocalService dlFileEntryLocalService;
    @Reference
    private AssetCategoryLocalService assetCategoryLocalService;
    @Reference
    private UserGroupRoleLocalService userGroupRoleLocalService;
    @Reference
    private RoleLocalService roleLocalService;
}