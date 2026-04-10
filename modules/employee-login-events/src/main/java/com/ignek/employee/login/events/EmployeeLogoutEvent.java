package com.ignek.employee.login.events;

import com.ignek.employee.login.constants.EmployeeLoginConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component(
        property = {
            "key=logout.events.post"
        },
        service = LifecycleAction.class
)

public class EmployeeLogoutEvent implements LifecycleAction{
    private static final Log log = LogFactoryUtil.getLog(EmployeeLogoutEvent.class);

    @Reference
    private Portal portal;
    @Reference
    private GroupLocalService groupLocalService;
    @Reference
    private ObjectDefinitionLocalService objectDefinitionLocalService;
    @Reference
    private ObjectEntryLocalService objectEntryLocalService;

    @Override
    public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
        try{
            HttpServletRequest request = lifecycleEvent.getRequest();
            String ipAddress = request.getRemoteAddr();
            log.info("Client IP Address : " + ipAddress);

            long companyId = portal.getUser(request).getCompanyId();
            long userId = portal.getUser(request).getUserId();
            String userEmailAddress = portal.getUser(request).getEmailAddress();

            Group group = groupLocalService.getGroup(companyId, EmployeeLoginConstants.IGNEK_INTRANET);
            long groupId = group.getGroupId();

            ObjectDefinition objectDefinition = objectDefinitionLocalService.fetchObjectDefinitionByExternalReferenceCode(EmployeeLoginConstants.ACTIVITY, companyId);

            if (Validator.isNotNull(objectDefinition)) {
                ServiceContext serviceContext = new ServiceContext();
                Map<String, Serializable> values = new HashMap<>();
                values.put(EmployeeLoginConstants.ACTIVITY_TYPE, EmployeeLoginConstants.LOGOUT);
                values.put(EmployeeLoginConstants.DETAILS, userEmailAddress);
                values.put(EmployeeLoginConstants.IP_ADDRESS, ipAddress);
                values.put(EmployeeLoginConstants.ACTIVITY_USER_ID, userId);

                ObjectEntry objectEntry = objectEntryLocalService.addObjectEntry(userId, groupId, objectDefinition.getObjectDefinitionId(), values, serviceContext);
                log.info("ObjectEntry created for logout event & Id :" + objectEntry.getObjectEntryId());
            }
        } catch (Exception e) {
            log.info("Error occurred while generate the logout activityEntry ", e );
        }
    }
}
