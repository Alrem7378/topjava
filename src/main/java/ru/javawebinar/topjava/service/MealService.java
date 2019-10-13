package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {
    @Autowired
    private MealRepository repository;


   /* public MealService(MealRepository repository) {
        this.repository = repository;
    }*/

    public Meal create(Meal meal, int userId) {
        checkNew(meal);
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        return (List<Meal>) repository.getAll(userId);
    }

    public void update(Meal meal, int id, int userId) throws NotFoundException {
        assureIdConsistent(meal, id);
        checkNotFoundWithId(repository.save(meal, userId), id);
    }


}