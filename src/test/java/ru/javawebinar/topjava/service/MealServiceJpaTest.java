package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveJpaProfileResolver;

@ActiveProfiles(resolver = ActiveJpaProfileResolver.class)
public class MealServiceJpaTest extends MealServiceTest {
}
