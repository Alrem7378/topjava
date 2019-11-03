package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveJdbcProfileResolver;

@ActiveProfiles(resolver = ActiveJdbcProfileResolver.class)
public class UserServiceJdbcTest extends UserServiceTest {
}
