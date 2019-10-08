package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMemoryRepo implements RepoMeal {
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private AtomicInteger mealCurId = new AtomicInteger();

    @Override
    public void add(Meal meal) {
        int id = getCurId();
        meal.setId(id);
        meals.put(id, meal);
    }

    @Override
    public void delete(int mealId) {
        meals.remove(mealId);
    }

    @Override
    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);

    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(int id) {
        return meals.get(id);
    }

    public int getCurId(){
        return mealCurId.incrementAndGet();
    }

}
