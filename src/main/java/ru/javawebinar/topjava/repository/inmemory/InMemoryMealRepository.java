package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            log.info("user id {}, save {}", userId, meal);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage

        log.info("user id {}, save {}", userId, meal);
        if (meal.getId() > 0 && meal.getId() <= counter.get()
                && checkUserId(repository.get(meal.getId()), userId)) {
            meal.setUserId(userId);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("user id {}, delete {}", userId, id);
        return checkUserId(repository.get(id), userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        log.info("user id {}, get {}", userId, id);
        return checkUserId(meal, userId) ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("user id {}", userId);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((o1, o2) -> -o1.getDateTime().compareTo(o2.getDateTime()))
                .collect(Collectors.toList());
    }

    private boolean checkUserId(Meal meal, int userId) {
        return meal.getUserId() == userId;
    }
}

