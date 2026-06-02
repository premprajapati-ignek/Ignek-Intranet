package com.ignek.employee.rest.internal.graphql.mutation.v1_0;

import com.ignek.employee.rest.dto.v1_0.Employee;
import com.ignek.employee.rest.resource.v1_0.EmployeeResource;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author hp
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setEmployeeResourceComponentServiceObjects(
		ComponentServiceObjects<EmployeeResource>
			employeeResourceComponentServiceObjects) {

		_employeeResourceComponentServiceObjects =
			employeeResourceComponentServiceObjects;
	}

	@GraphQLField
	public Employee deleteEmployee(
			@GraphQLName("employeeId") Integer employeeId)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.deleteEmployee(employeeId));
	}

	@GraphQLField
	public Response deleteEmployeeBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.deleteEmployeeBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Employee createEmployee(@GraphQLName("employee") Employee employee)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.postEmployee(employee));
	}

	@GraphQLField
	public Response createEmployeeBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.postEmployeeBatch(
				callbackURL, object));
	}

	@GraphQLField
	public Employee updateEmployee(
			@GraphQLName("employeeId") Integer employeeId,
			@GraphQLName("employee") Employee employee)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.putEmployee(
				employeeId, employee));
	}

	@GraphQLField
	public Response updateEmployeeBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_employeeResourceComponentServiceObjects,
			this::_populateResourceContext,
			employeeResource -> employeeResource.putEmployeeBatch(
				callbackURL, object));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(EmployeeResource employeeResource)
		throws Exception {

		employeeResource.setContextAcceptLanguage(_acceptLanguage);
		employeeResource.setContextCompany(_company);
		employeeResource.setContextHttpServletRequest(_httpServletRequest);
		employeeResource.setContextHttpServletResponse(_httpServletResponse);
		employeeResource.setContextUriInfo(_uriInfo);
		employeeResource.setContextUser(_user);
		employeeResource.setGroupLocalService(_groupLocalService);
		employeeResource.setRoleLocalService(_roleLocalService);

		employeeResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects<EmployeeResource>
		_employeeResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, com.liferay.portal.kernel.search.Sort[]>
		_sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;
	private VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource;

}