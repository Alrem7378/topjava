package ru.javawebinar.topjava.util;

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
        List<UserMealWithExceed> list = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0, 0, 0), 2000);
        list.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumMap = new HashMap<>();
        mealList.forEach(u -> {
            LocalDate dateMeal = u.getDateTime().toLocalDate();
            caloriesSumMap.merge(dateMeal, u.getCalories(), Integer::sum);
        });

        List<UserMealWithExceed> withExceedList = new ArrayList<>();
        mealList.forEach(u -> {
            LocalTime timeMeal = u.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(timeMeal, startTime, endTime)) {
                withExceedList.add(new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), caloriesSumMap.get(u.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return withExceedList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(int caloriesPerDay, List<UserMeal> mealList, LocalTime startTime, LocalTime endTime) {
        Map<LocalDate, Integer> caloriesSumMap = mealList.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter((u) -> TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime))
                .map(u -> new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), caloriesSumMap.get(u.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
