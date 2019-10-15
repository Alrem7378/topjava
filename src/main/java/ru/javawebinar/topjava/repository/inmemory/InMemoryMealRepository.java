package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);


    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("user id {}, save {}", userId, meal);
            getRepoByUserId(userId).put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage

        log.info("user id {}, save {}", userId, meal);
        return getRepoByUserId(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("user id {}, delete {}", userId, id);
        return getRepoByUserId(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("user id {}, get {}", userId, id);
        return getRepoByUserId(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("user id {}", userId);
        return getAllFilteredByUser(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilteredByDate(int userId, LocalDate starDate, LocalDate endDate) {
        log.info("user id {}", userId);
        return getAllFilteredByUser(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), starDate, endDate));
    }

    private List<Meal> getAllFilteredByUser(int userId, Predicate<Meal> filter) {
        log.info("user id {}", userId);
        return getRepoByUserId(userId).values().stream()
                .filter(filter)
                .sorted((o1, o2) -> -o1.getDateTime().compareTo(o2.getDateTime()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getRepoByUserId(int userId) {
        return repository.computeIfAbsent(userId, id -> new HashMap<>());
    }

}

