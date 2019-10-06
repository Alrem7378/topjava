<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<html>
<head>
    <title>Meals</title>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>Дата/время</th>
        <th>Прием пищи</th>
        <th>Калории</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsList}" var="meal">
        <tr>
        <tr style="color:${meal.excess ? 'red' : 'green'}">
            <td>${dateTimeFormatter.format(meal.dateTime)} </td>
            <td>${meal.description}" </td>
            <td>${meal.calories}" </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
