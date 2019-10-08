package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface RepoMeal {
    void add(Meal meal);
    void delete(int id);
    void update(Meal meal);
    List<Meal> getAll();
    Meal findById(int Id);
}
