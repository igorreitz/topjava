package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test(expected = NotFoundException.class)
    public void getForeignMeal() {
        service.get(MEAL_ID_1, ADMIN.getId());
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        service.delete(MEAL_ID_1, ADMIN.getId());
    }

    @Test
    public void get() {
        service.get(MEAL_ID_1, USER.getId());
    }

    @Test
    public void getAll() {
        List<Meal> testData = new ArrayList<>();
        testData.add(meal1);
        testData.add(meal2);
        testData.add(meal3);
        testData.add(meal4);
        testData.add(meal5);
        testData.add(meal6);
        Collections.sort(testData, Comparator.comparing(Meal::getDateTime).reversed());

        List<Meal> actualData = service.getAll(USER.getId());

        assertMatch(actualData, testData);
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_1, USER.getId());
    }

    @Test
    public void update() {
        Meal updated = new Meal(meal1);
        service.update(updated, USER.getId());
        assertMatch(service.get(MEAL_ID_1, USER.getId()), updated);
    }
}