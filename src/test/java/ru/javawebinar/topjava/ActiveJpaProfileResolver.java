package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;

public class ActiveJpaProfileResolver implements ActiveProfilesResolver {
    @Override
    public String[] resolve(Class<?> testClass) {
        return new String[]{Profiles.getActiveDbProfile(), Profiles.JPA};
    }
}
