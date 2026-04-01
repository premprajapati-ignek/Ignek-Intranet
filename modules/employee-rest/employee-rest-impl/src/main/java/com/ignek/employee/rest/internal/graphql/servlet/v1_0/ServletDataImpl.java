package com.ignek.employee.rest.internal.graphql.servlet.v1_0;

import com.ignek.employee.rest.internal.graphql.mutation.v1_0.Mutation;
import com.ignek.employee.rest.internal.graphql.query.v1_0.Query;
import com.ignek.employee.rest.internal.resource.v1_0.EmployeeResourceImpl;
import com.ignek.employee.rest.resource.v1_0.EmployeeResource;

import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author hp
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setEmployeeResourceComponentServiceObjects(
			_employeeResourceComponentServiceObjects);

		Query.setEmployeeResourceComponentServiceObjects(
			_employeeResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "EmployeeRest";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/employee-rest-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#deleteEmployee",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "deleteEmployee"));
					put(
						"mutation#deleteEmployeeBatch",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "deleteEmployeeBatch"));
					put(
						"mutation#createEmployee",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "postEmployee"));
					put(
						"mutation#createEmployeeBatch",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "postEmployeeBatch"));
					put(
						"mutation#updateEmployee",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "putEmployee"));
					put(
						"mutation#updateEmployeeBatch",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "putEmployeeBatch"));

					put(
						"query#employee",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "getEmployee"));
					put(
						"query#employees",
						new ObjectValuePair<>(
							EmployeeResourceImpl.class, "getEmployeesPage"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<EmployeeResource>
		_employeeResourceComponentServiceObjects;

}