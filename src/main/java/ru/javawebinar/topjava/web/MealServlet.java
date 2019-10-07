package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealMemoryImp;
import ru.javawebinar.topjava.repository.RepoMeal;
import ru.javawebinar.topjava.util.MealsUtil;
import sun.rmi.runtime.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private RepoMeal repoMeal;

    @Override
    public void init() throws ServletException {
        repoMeal = new MealMemoryImp();
        MealsUtil.getInitmap(repoMeal);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            mealsPageForward(request, response);
            return;
        }
        Meal meal = null;

        switch (action) {
            case "delete":
                repoMeal.deleteMeal(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = repoMeal.findById(Integer.parseInt(request.getParameter("id")));
                break;
            case "new":
                break;
            default:
                mealsPageForward(request, response);
                return;
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String dateTime = request.getParameter("datetime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        if (id.equals("")) {
            repoMeal.addMeal(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            repoMeal.updateMeal(meal);
        }

        response.sendRedirect("meals");
    }

    private void mealsPageForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = MealsUtil.getFiltered(repoMeal.getAllMeal(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealsList", mealsTo);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
