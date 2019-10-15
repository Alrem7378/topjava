package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;


    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(LocalDate starDate, LocalDate endDate, LocalTime starTime, LocalTime endTime) {
        log.info("getAllFiltered");
        starDate = starDate != null ? starDate: LocalDate.MIN;
        endDate = endDate != null ? endDate: LocalDate.MAX;
        starTime = starTime != null ? starTime: LocalTime.MIN;
        endTime = endTime != null ? endTime: LocalTime.MAX;
        log.info("startDate {}, endDate {}, startTime {}, endTime {}", starDate, endDate, starTime, endTime);
        return MealsUtil.getFilteredTos(service.getAllFiltered(authUserId(), starDate, endDate), MealsUtil.DEFAULT_CALORIES_PER_DAY, starTime, endTime);
    }

    public void delete(int id) {
        log.info("delete");
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update");
        assureIdConsistent(meal, id);
        service.update(meal, id, authUserId());
    }

    public Meal get(int id) {
        log.info("get");
        return service.get(id, authUserId());
    }

    public void create(Meal meal) {
        log.info("create");
        checkNew(meal);
        service.create(meal, authUserId());
    }


}