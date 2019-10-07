package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MealMemoryImp implements RepoMeal {
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static AtomicInteger mealCurId = new AtomicInteger();

    @Override
    public void addMeal(Meal meal) {
        int id = getMealCurId();
        meal.setId(id);
        meals.put(id, meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        meals.remove(mealId);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.replace(meal.getId(), meal);

    }

    @Override
    public List<Meal> getAllMeal() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(int id) {
        return meals.get(id);
    }

    public int getMealCurId(){
        return mealCurId.incrementAndGet();
    }

}
