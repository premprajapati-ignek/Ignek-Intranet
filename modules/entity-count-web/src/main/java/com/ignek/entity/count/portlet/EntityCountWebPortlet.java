package com.ignek.entity.count.portlet;

import com.ignek.entity.count.constants.EntityCountWebPortletKeys;

import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;

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
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		try {
			PortletPreferences portletPreferences = renderRequest.getPreferences();
			String title = portletPreferences.getValue("title","Employees");
			System.out.println(title);

			if (title.equals("Employees")){
				long companyId = PortalUtil.getDefaultCompanyId();
				Role employeeRole = RoleLocalServiceUtil.getRole(companyId, "Site Employee");
				int employeeCount = UserLocalServiceUtil.getRoleUsersCount(employeeRole.getRoleId());
				renderRequest.setAttribute("count", employeeCount);
			} else if (title.equals("Technologies")) {
				long journalArticlesCount = JournalArticleLocalServiceUtil.getJournalArticlesCount();
				renderRequest.setAttribute("count", journalArticlesCount);
			} else if (title.equals("Images")) {
				int imagesCount = DLFileEntryLocalServiceUtil.getDLFileEntriesCount();
				renderRequest.setAttribute("count", imagesCount);
			} else if (title.equals("Users")) {
				long usersCount = UserLocalServiceUtil.getUsersCount();
				renderRequest.setAttribute("count", usersCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.render(renderRequest, renderResponse);
	}
}