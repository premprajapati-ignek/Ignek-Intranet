package com.ignek.portal.user.service.wrapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceWrapper;
import org.osgi.service.component.annotations.Component;
import java.util.Locale;

@Component(
		property = {
		},
		service = ServiceWrapper.class
)

public class CustomUserLocalServiceWrapper extends UserLocalServiceWrapper {
	private static final Log log = LogFactoryUtil.getLog(CustomUserLocalServiceWrapper.class);

	public CustomUserLocalServiceWrapper() {
		super(null);
	}

	@Override
	public User addUser(long creatorUserId, long companyId, boolean autoPassword, String password1, String password2, boolean autoScreenName, String screenName, String emailAddress, Locale locale, String firstName, String middleName, String lastName, long prefixListTypeId, long suffixListTypeId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, String jobTitle, int type, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail, ServiceContext serviceContext) throws PortalException {

		User user = super.addUser(creatorUserId, companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, locale, firstName, middleName, lastName, prefixListTypeId, suffixListTypeId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, type, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

		boolean isEmployee = emailAddress.endsWith("@ignek.com") || emailAddress.endsWith(".ignek@gmail.com");

		try{
			user.getExpandoBridge().setAttribute("isEmployee", isEmployee);
		} catch (Exception e) {
			log.error("Failed to set expando bridge attribute", e);
		}

		return user;
	}
}