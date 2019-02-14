<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Meals</title>
    <style>
        .normal {color: green}
        .exceeded {color: red}
    </style>
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Количество калорий</th>
        <th colspan=2>Действие</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealList}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.excess ? 'exceeded' : 'normal'}">
            <td>
                <%=TimeUtil.toString(meal.getDateTime())%>
            </td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=update&mealId=<c:out value="${meal.id}"/>">Изменить</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=create">Add Meal</a></p>
</body>
</html>