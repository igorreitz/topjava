<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <c:choose>
        <c:when test="${mealForm['new']}">
            <h2><spring:message code="meal.add_form"/></h2>
        </c:when>
        <c:otherwise>
            <h2><spring:message code="meal.edit_form"/></h2>
        </c:otherwise>
    </c:choose>
    <hr>
    <spring:url value="save" var="userActionUrl"/>
    <form:form method="post" modelAttribute="mealForm" action="${userActionUrl}">
        <input type="hidden" name="id" value="${mealForm.id}">
        <dl>
            <dt><spring:message code="meal.date_time"/>:</dt>
            <spring:bind path="dateTime">
                <dd>
                    <form:input path="dateTime" type="dateTime-local"
                                id="dateTime" placeholder="Date Time"/>
                </dd>
            </spring:bind>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${mealForm.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${mealForm.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="meal.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="meal.cancel"/></button>
    </form:form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
