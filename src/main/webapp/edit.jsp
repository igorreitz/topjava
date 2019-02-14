<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add|Edit meal</title>
</head>
<body>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<form method="POST" action='meals'>
    <input type="hidden" name="mealId" value="${meal.id}"/> <br/>
    Description : <input type="text" name="description" value="${meal.description}"/> <br/>
    Calories : <input type="number" value="${meal.calories}" name="calories"/> <br/>
    Date : <input type="datetime-local" name="dateTime" value="${ meal.dateTime }"/> <br/>

    <button type="submit">Save</button>
    <button onclick="window.history.back()">Cancel</button>

</form>
</body>
</html>
