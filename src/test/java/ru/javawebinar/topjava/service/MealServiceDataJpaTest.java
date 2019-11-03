package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDataJpaProfileResolver;

@ActiveProfiles(resolver = ActiveDataJpaProfileResolver.class)
public class MealServiceDataJpaTest extends MealServiceTest {
}
