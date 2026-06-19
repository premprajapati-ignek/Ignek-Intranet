create table Custom_Employee (
	uuid_ VARCHAR(75) null,
	employeeId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	firstName VARCHAR(75) null,
	lastName VARCHAR(75) null,
	emailAddress VARCHAR(75) null,
	phoneNumber VARCHAR(75) null,
	addressLine1 VARCHAR(75) null,
	addressLine2 VARCHAR(75) null,
	city VARCHAR(75) null,
	zipCode VARCHAR(75) null,
	designation VARCHAR(75) null
);