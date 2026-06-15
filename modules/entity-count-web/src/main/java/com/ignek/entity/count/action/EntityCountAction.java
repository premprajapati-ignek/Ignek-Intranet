package com.ignek.entity.count.action;

import com.ignek.entity.count.constants.EntityCountWebPortletKeys;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component(
        property = {
                "javax.portlet.name=" + EntityCountWebPortletKeys.ENTITY_COUNT_WEB
        },
        service = ConfigurationAction.class
)
public class EntityCountAction extends DefaultConfigurationAction {

    @Override
    public void processAction(
            PortletConfig portletConfig,
            ActionRequest actionRequest,
            ActionResponse actionResponse)
            throws Exception {

        String title = ParamUtil.getString(actionRequest, "title");

        setPreference(actionRequest, "title", title);

        super.processAction(portletConfig, actionRequest, actionResponse);
    }
}
