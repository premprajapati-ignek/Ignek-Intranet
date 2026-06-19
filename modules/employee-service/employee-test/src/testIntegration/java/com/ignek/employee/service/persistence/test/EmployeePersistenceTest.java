/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.ignek.employee.service.persistence.test;

import com.ignek.employee.exception.NoSuchEmployeeException;
import com.ignek.employee.model.Employee;
import com.ignek.employee.service.EmployeeLocalServiceUtil;
import com.ignek.employee.service.persistence.EmployeePersistence;
import com.ignek.employee.service.persistence.EmployeeUtil;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class EmployeePersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.ignek.employee.service"));

	@Before
	public void setUp() {
		_persistence = EmployeeUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<Employee> iterator = _employees.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Employee employee = _persistence.create(pk);

		Assert.assertNotNull(employee);

		Assert.assertEquals(employee.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Employee newEmployee = addEmployee();

		_persistence.remove(newEmployee);

		Employee existingEmployee = _persistence.fetchByPrimaryKey(
			newEmployee.getPrimaryKey());

		Assert.assertNull(existingEmployee);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addEmployee();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Employee newEmployee = _persistence.create(pk);

		newEmployee.setUuid(RandomTestUtil.randomString());

		newEmployee.setGroupId(RandomTestUtil.nextLong());

		newEmployee.setCompanyId(RandomTestUtil.nextLong());

		newEmployee.setUserId(RandomTestUtil.nextLong());

		newEmployee.setUserName(RandomTestUtil.randomString());

		newEmployee.setCreateDate(RandomTestUtil.nextDate());

		newEmployee.setModifiedDate(RandomTestUtil.nextDate());

		newEmployee.setFirstName(RandomTestUtil.randomString());

		newEmployee.setLastName(RandomTestUtil.randomString());

		newEmployee.setEmailAddress(RandomTestUtil.randomString());

		newEmployee.setPhoneNumber(RandomTestUtil.randomString());

		newEmployee.setAddressLine1(RandomTestUtil.randomString());

		newEmployee.setAddressLine2(RandomTestUtil.randomString());

		newEmployee.setCity(RandomTestUtil.randomString());

		newEmployee.setZipCode(RandomTestUtil.randomString());

		newEmployee.setDesignation(RandomTestUtil.randomString());

		_employees.add(_persistence.update(newEmployee));

		Employee existingEmployee = _persistence.findByPrimaryKey(
			newEmployee.getPrimaryKey());

		Assert.assertEquals(existingEmployee.getUuid(), newEmployee.getUuid());
		Assert.assertEquals(
			existingEmployee.getEmployeeId(), newEmployee.getEmployeeId());
		Assert.assertEquals(
			existingEmployee.getGroupId(), newEmployee.getGroupId());
		Assert.assertEquals(
			existingEmployee.getCompanyId(), newEmployee.getCompanyId());
		Assert.assertEquals(
			existingEmployee.getUserId(), newEmployee.getUserId());
		Assert.assertEquals(
			existingEmployee.getUserName(), newEmployee.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingEmployee.getCreateDate()),
			Time.getShortTimestamp(newEmployee.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingEmployee.getModifiedDate()),
			Time.getShortTimestamp(newEmployee.getModifiedDate()));
		Assert.assertEquals(
			existingEmployee.getFirstName(), newEmployee.getFirstName());
		Assert.assertEquals(
			existingEmployee.getLastName(), newEmployee.getLastName());
		Assert.assertEquals(
			existingEmployee.getEmailAddress(), newEmployee.getEmailAddress());
		Assert.assertEquals(
			existingEmployee.getPhoneNumber(), newEmployee.getPhoneNumber());
		Assert.assertEquals(
			existingEmployee.getAddressLine1(), newEmployee.getAddressLine1());
		Assert.assertEquals(
			existingEmployee.getAddressLine2(), newEmployee.getAddressLine2());
		Assert.assertEquals(existingEmployee.getCity(), newEmployee.getCity());
		Assert.assertEquals(
			existingEmployee.getZipCode(), newEmployee.getZipCode());
		Assert.assertEquals(
			existingEmployee.getDesignation(), newEmployee.getDesignation());
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G("", RandomTestUtil.nextLong());

		_persistence.countByUUID_G("null", 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Employee newEmployee = addEmployee();

		Employee existingEmployee = _persistence.findByPrimaryKey(
			newEmployee.getPrimaryKey());

		Assert.assertEquals(existingEmployee, newEmployee);
	}

	@Test(expected = NoSuchEmployeeException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<Employee> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"Custom_Employee", "uuid", true, "employeeId", true, "groupId",
			true, "companyId", true, "userId", true, "userName", true,
			"createDate", true, "modifiedDate", true, "firstName", true,
			"lastName", true, "emailAddress", true, "phoneNumber", true,
			"addressLine1", true, "addressLine2", true, "city", true, "zipCode",
			true, "designation", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Employee newEmployee = addEmployee();

		Employee existingEmployee = _persistence.fetchByPrimaryKey(
			newEmployee.getPrimaryKey());

		Assert.assertEquals(existingEmployee, newEmployee);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Employee missingEmployee = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingEmployee);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		Employee newEmployee1 = addEmployee();
		Employee newEmployee2 = addEmployee();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newEmployee1.getPrimaryKey());
		primaryKeys.add(newEmployee2.getPrimaryKey());

		Map<Serializable, Employee> employees = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(2, employees.size());
		Assert.assertEquals(
			newEmployee1, employees.get(newEmployee1.getPrimaryKey()));
		Assert.assertEquals(
			newEmployee2, employees.get(newEmployee2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, Employee> employees = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(employees.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		Employee newEmployee = addEmployee();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newEmployee.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, Employee> employees = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, employees.size());
		Assert.assertEquals(
			newEmployee, employees.get(newEmployee.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, Employee> employees = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertTrue(employees.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		Employee newEmployee = addEmployee();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newEmployee.getPrimaryKey());

		Map<Serializable, Employee> employees = _persistence.fetchByPrimaryKeys(
			primaryKeys);

		Assert.assertEquals(1, employees.size());
		Assert.assertEquals(
			newEmployee, employees.get(newEmployee.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			EmployeeLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<Employee>() {

				@Override
				public void performAction(Employee employee) {
					Assert.assertNotNull(employee);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		Employee newEmployee = addEmployee();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Employee.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"employeeId", newEmployee.getEmployeeId()));

		List<Employee> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Employee existingEmployee = result.get(0);

		Assert.assertEquals(existingEmployee, newEmployee);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Employee.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"employeeId", RandomTestUtil.nextLong()));

		List<Employee> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		Employee newEmployee = addEmployee();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Employee.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("employeeId"));

		Object newEmployeeId = newEmployee.getEmployeeId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"employeeId", new Object[] {newEmployeeId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingEmployeeId = result.get(0);

		Assert.assertEquals(existingEmployeeId, newEmployeeId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Employee.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("employeeId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"employeeId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		Employee newEmployee = addEmployee();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newEmployee.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		Employee newEmployee = addEmployee();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Employee.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"employeeId", newEmployee.getEmployeeId()));

		List<Employee> result = _persistence.findWithDynamicQuery(dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(Employee employee) {
		Assert.assertEquals(
			employee.getUuid(),
			ReflectionTestUtil.invoke(
				employee, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(employee.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				employee, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));
	}

	protected Employee addEmployee() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Employee employee = _persistence.create(pk);

		employee.setUuid(RandomTestUtil.randomString());

		employee.setGroupId(RandomTestUtil.nextLong());

		employee.setCompanyId(RandomTestUtil.nextLong());

		employee.setUserId(RandomTestUtil.nextLong());

		employee.setUserName(RandomTestUtil.randomString());

		employee.setCreateDate(RandomTestUtil.nextDate());

		employee.setModifiedDate(RandomTestUtil.nextDate());

		employee.setFirstName(RandomTestUtil.randomString());

		employee.setLastName(RandomTestUtil.randomString());

		employee.setEmailAddress(RandomTestUtil.randomString());

		employee.setPhoneNumber(RandomTestUtil.randomString());

		employee.setAddressLine1(RandomTestUtil.randomString());

		employee.setAddressLine2(RandomTestUtil.randomString());

		employee.setCity(RandomTestUtil.randomString());

		employee.setZipCode(RandomTestUtil.randomString());

		employee.setDesignation(RandomTestUtil.randomString());

		_employees.add(_persistence.update(employee));

		return employee;
	}

	private List<Employee> _employees = new ArrayList<Employee>();
	private EmployeePersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}