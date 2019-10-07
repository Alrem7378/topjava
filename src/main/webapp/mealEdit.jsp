<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EditMeal</title>
</head>
<body>
<h1>${meal == null ? "Добавить" : "Редактировать"}</h1>
<form method="post">
    <input type="hidden" name="id" value="${meal.id}" />
    <table>
        <tr>
            <td><label>Дата/Время</label></td>
            <td><label>Описание</label></td>
            <td><label>Калории</label></td>
        </tr>

        <tr>
            <td><input type="datetime-local" name="datetime"
                       value="${meal == null ? "" : meal.getDateTime().toString()}"  id="datetime"></td>
            <td><input type="text" name="description"
                       value="${meal == null ? "" : meal.description}" id="description"></td>
            <td><input type="number" name="calories" min="0"
                       value="${meal == null ? "" : meal.calories}" id="calories"></td>
            <td><input type="submit"></td>
        </tr>
    </table>
    <p></p>
    <a href="meals">Back</a>
</form>
</body>
</html>
