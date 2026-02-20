<%@ include file="/init.jsp" %>

<div class="entity-count-web">
    <div class="records-card-container">
      <div class="card-information">
        <div class="card-img-container">
          <img class="card-img" src="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/asset/" + title + ".png") %>" alt="#" />
        </div>
        <b class="card-title poppins-medium"><%=title%></b>
      </div>
      <div class="card-acvtive-number">
        <span class="poppins-bold">${count}</span>
      </div>
    </div>
</div>