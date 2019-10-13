package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;


   /* public MealRestController(MealService service) {
        this.service = service;
    }*/

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }

    public void delete(int id) {
        log.info("delete");
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update");
        service.update(meal, id, authUserId());
    }

    public Meal get(int id) {
        log.info("get");
        return service.get(id, authUserId());
    }

    public void create(Meal meal) {
        log.info("create");
        service.create(meal, authUserId());
    }


}