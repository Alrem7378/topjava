package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {
    @Test
    public void getWithUser() {
        Meal meal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL1);
        UserTestData.assertMatch(meal.getUser(), ADMIN);
    }
}
