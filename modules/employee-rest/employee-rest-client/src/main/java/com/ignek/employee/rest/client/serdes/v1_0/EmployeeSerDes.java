package com.ignek.employee.rest.client.serdes.v1_0;

import com.ignek.employee.rest.client.dto.v1_0.Employee;
import com.ignek.employee.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author hp
 * @generated
 */
@Generated("")
public class EmployeeSerDes {

	public static Employee toDTO(String json) {
		EmployeeJSONParser employeeJSONParser = new EmployeeJSONParser();

		return employeeJSONParser.parseToDTO(json);
	}

	public static Employee[] toDTOs(String json) {
		EmployeeJSONParser employeeJSONParser = new EmployeeJSONParser();

		return employeeJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Employee employee) {
		if (employee == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (employee.getAddressLine1() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"addressLine1\": ");

			sb.append("\"");

			sb.append(_escape(employee.getAddressLine1()));

			sb.append("\"");
		}

		if (employee.getAddressLine2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"addressLine2\": ");

			sb.append("\"");

			sb.append(_escape(employee.getAddressLine2()));

			sb.append("\"");
		}

		if (employee.getCity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"city\": ");

			sb.append("\"");

			sb.append(_escape(employee.getCity()));

			sb.append("\"");
		}

		if (employee.getDesignation() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"designation\": ");

			sb.append("\"");

			sb.append(_escape(employee.getDesignation()));

			sb.append("\"");
		}

		if (employee.getEmailAddress() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"emailAddress\": ");

			sb.append("\"");

			sb.append(_escape(employee.getEmailAddress()));

			sb.append("\"");
		}

		if (employee.getEmployeeId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"employeeId\": ");

			sb.append(employee.getEmployeeId());
		}

		if (employee.getFirstName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"firstName\": ");

			sb.append("\"");

			sb.append(_escape(employee.getFirstName()));

			sb.append("\"");
		}

		if (employee.getLastName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"lastName\": ");

			sb.append("\"");

			sb.append(_escape(employee.getLastName()));

			sb.append("\"");
		}

		if (employee.getPhoneNumber() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"phoneNumber\": ");

			sb.append("\"");

			sb.append(_escape(employee.getPhoneNumber()));

			sb.append("\"");
		}

		if (employee.getZipCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"zipCode\": ");

			sb.append("\"");

			sb.append(_escape(employee.getZipCode()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		EmployeeJSONParser employeeJSONParser = new EmployeeJSONParser();

		return employeeJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Employee employee) {
		if (employee == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (employee.getAddressLine1() == null) {
			map.put("addressLine1", null);
		}
		else {
			map.put("addressLine1", String.valueOf(employee.getAddressLine1()));
		}

		if (employee.getAddressLine2() == null) {
			map.put("addressLine2", null);
		}
		else {
			map.put("addressLine2", String.valueOf(employee.getAddressLine2()));
		}

		if (employee.getCity() == null) {
			map.put("city", null);
		}
		else {
			map.put("city", String.valueOf(employee.getCity()));
		}

		if (employee.getDesignation() == null) {
			map.put("designation", null);
		}
		else {
			map.put("designation", String.valueOf(employee.getDesignation()));
		}

		if (employee.getEmailAddress() == null) {
			map.put("emailAddress", null);
		}
		else {
			map.put("emailAddress", String.valueOf(employee.getEmailAddress()));
		}

		if (employee.getEmployeeId() == null) {
			map.put("employeeId", null);
		}
		else {
			map.put("employeeId", String.valueOf(employee.getEmployeeId()));
		}

		if (employee.getFirstName() == null) {
			map.put("firstName", null);
		}
		else {
			map.put("firstName", String.valueOf(employee.getFirstName()));
		}

		if (employee.getLastName() == null) {
			map.put("lastName", null);
		}
		else {
			map.put("lastName", String.valueOf(employee.getLastName()));
		}

		if (employee.getPhoneNumber() == null) {
			map.put("phoneNumber", null);
		}
		else {
			map.put("phoneNumber", String.valueOf(employee.getPhoneNumber()));
		}

		if (employee.getZipCode() == null) {
			map.put("zipCode", null);
		}
		else {
			map.put("zipCode", String.valueOf(employee.getZipCode()));
		}

		return map;
	}

	public static class EmployeeJSONParser extends BaseJSONParser<Employee> {

		@Override
		protected Employee createDTO() {
			return new Employee();
		}

		@Override
		protected Employee[] createDTOArray(int size) {
			return new Employee[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "addressLine1")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "addressLine2")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "city")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "designation")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "emailAddress")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "employeeId")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "firstName")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "lastName")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "phoneNumber")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "zipCode")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			Employee employee, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "addressLine1")) {
				if (jsonParserFieldValue != null) {
					employee.setAddressLine1((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "addressLine2")) {
				if (jsonParserFieldValue != null) {
					employee.setAddressLine2((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "city")) {
				if (jsonParserFieldValue != null) {
					employee.setCity((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "designation")) {
				if (jsonParserFieldValue != null) {
					employee.setDesignation((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "emailAddress")) {
				if (jsonParserFieldValue != null) {
					employee.setEmailAddress((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "employeeId")) {
				if (jsonParserFieldValue != null) {
					employee.setEmployeeId(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "firstName")) {
				if (jsonParserFieldValue != null) {
					employee.setFirstName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "lastName")) {
				if (jsonParserFieldValue != null) {
					employee.setLastName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "phoneNumber")) {
				if (jsonParserFieldValue != null) {
					employee.setPhoneNumber((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "zipCode")) {
				if (jsonParserFieldValue != null) {
					employee.setZipCode((String)jsonParserFieldValue);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}