package com.ignek.employee.listener;

import com.ignek.employee.constants.EmployeeModelListenerConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component(
    immediate = true,
    service = ModelListener.class
)

public class UserGroupRoleModelListener extends BaseModelListener<UserGroupRole> {
    private static final Log log = LogFactoryUtil.getLog(UserGroupRoleModelListener.class);
    @Reference
    private UserLocalService userLocalService;
    @Reference
    private ObjectDefinitionLocalService objectDefinitionLocalService;
    @Reference
    private ObjectEntryLocalService objectEntryLocalService;

    @Override
    public void onAfterCreate(UserGroupRole model) throws ModelListenerException {
        addActivityObjectEntry(model, EmployeeModelListenerConstants.ROLE_ASSIGNED);
        super.onAfterCreate(model);
    }

    @Override
    public void onAfterRemove(UserGroupRole model) throws ModelListenerException {
        addActivityObjectEntry(model, EmployeeModelListenerConstants.ROLE_REMOVED);
        super.onAfterRemove(model);
    }

    private void addActivityObjectEntry(UserGroupRole userGroupRole, String activityType){
        try {
            ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
            long companyId = userGroupRole.getCompanyId();
            long groupId = userGroupRole.getGroupId();
            long userId = serviceContext.getUserId();
            String emailAddress = userLocalService.fetchUserById(userId).getEmailAddress();
            String ipAddress = serviceContext.getRemoteAddr();
            log.info("Client IP Address : " + ipAddress);

            ObjectDefinition objectDefinition = objectDefinitionLocalService.fetchObjectDefinitionByExternalReferenceCode(EmployeeModelListenerConstants.ACTIVITY, companyId);
            long objectDefinitionId = objectDefinition.getObjectDefinitionId();

            if (Validator.isNotNull(objectDefinition)) {
                Map<String, Serializable> values = new HashMap<>();
                values.put(EmployeeModelListenerConstants.ACTIVITY_TYPE, activityType);
                values.put(EmployeeModelListenerConstants.DETAILS, emailAddress);
                values.put(EmployeeModelListenerConstants.IP_ADDRESS, ipAddress);
                values.put(EmployeeModelListenerConstants.ACTIVITY_USER_ID, userId);

                ObjectEntry objectEntry = objectEntryLocalService.addObjectEntry(userId, groupId, objectDefinitionId, values, serviceContext);
                log.info("ObjectEntry created for UserGroupRole event : " + objectEntry.getObjectEntryId());
            }
        } catch (Exception e) {
            log.info("Error occurred while generate the UserGroupRole activityEntry ", e);
        }
    }
}
