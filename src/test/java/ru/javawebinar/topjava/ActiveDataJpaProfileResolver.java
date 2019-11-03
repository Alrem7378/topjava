package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;

public class ActiveDataJpaProfileResolver implements ActiveProfilesResolver {
    @Override
    public String[] resolve(Class<?> testClass) {
        return new String[]{Profiles.getActiveDbProfile(), Profiles.DATAJPA};
    }
}
