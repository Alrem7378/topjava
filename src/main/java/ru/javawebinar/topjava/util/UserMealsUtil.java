package ru.javawebinar.topjava.util;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(2000, mealList, LocalTime.of(7, 0), LocalTime.of(12, 0));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMealWithExceed> list = new ArrayList<>();
        for (UserMeal u : mealList) {
            LocalDate dateMeal = u.getDateTime().toLocalDate();
            map.merge(dateMeal, u.getCalories(), (oldVal, newVal) -> oldVal + newVal);
        }

        for (UserMeal u : mealList) {
            LocalTime timeMeal = u.getDateTime().toLocalTime();
            boolean exceed = false;
            if (TimeUtil.isBetween(timeMeal, startTime, endTime)) {
                if (map.get(u.getDateTime().toLocalDate()) <= caloriesPerDay) exceed = true;
                list.add(new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), exceed));
            }
        }
        return list;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(int caloriesPerDay, List<UserMeal> mealList, LocalTime startTime, LocalTime endTime) {
        Map<LocalDate, Integer> map = mealList.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        List<UserMealWithExceed> list = mealList.stream()
                .filter((u) -> TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime))
                .map(u -> new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), map.get(u.getDateTime().toLocalDate()) <= caloriesPerDay))
                .collect(Collectors.toList());
        return list;
    }
}
