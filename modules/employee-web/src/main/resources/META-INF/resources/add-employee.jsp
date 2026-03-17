<%@ include file="/init.jsp" %>
<portlet:actionURL name="addEmployee" var="addEmployeeActionURL"/>

<div class="employee-form bg-white">
  <div class="m-2">
    <div class="container">
      <aui:form action="<%=addEmployeeActionURL %>" class="form-conta" name="employeeForm" method="POST">
        <div class="row">
          <div class="col-12">
            <p class="form-heading poppins-bold mb-3">Employee Form</p>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="firstName" label="First Name" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your first name">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="lastName" label="Last Name" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your last name">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-12">
            <div class="form-group">
                <aui:input name="designation" label="Designation" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your designation">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="emailAddress" label="Email" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your email">
                    <aui:validator name="email"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="phoneNumber" label="Phone" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your phone number">
                    <aui:validator name="digits"/>
                    <aui:validator name="maxLength">10</aui:validator>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="addressLine1" label="Address Line 1" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your house no / Bldg. / Appt.">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="addressLine2" label="Address Line 2" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your street / lane / area">
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="city" label="City" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your city">
                    <aui:validator name="alpha"/>
                </aui:input>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group">
                <aui:input name="zipCode" label="Post Code/ Zip Code" cssClass="form-control employee-form-input poppins-regular" placeholder="Enter your post code/ zip code">
                    <aui:validator name="digits"/>
                    <aui:validator name="maxLength">6</aui:validator>
                </aui:input>
            </div>
          </div>
        </div>
        <div class="d-flex flex-row-reverse">
          <aui:button type="submit" name="" value="SUBMIT" cssClass="employee-form-btn"></aui:button>
        </div>
      </aui:form>
    </div>
  </div>
</div>