package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface RepoMeal {
    void addMeal(Meal meal);
    void deleteMeal(int mealId);
    void updateMeal(Meal meal);
    List<Meal> getAllMeal();
    Meal findById(int Id);
}
