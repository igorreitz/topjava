package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_1 = START_SEQ + 2;
    public static final int MEAL_ID_2 = START_SEQ + 3;
    public static final int MEAL_ID_3 = START_SEQ + 4;
    public static final int MEAL_ID_4 = START_SEQ + 5;
    public static final int MEAL_ID_5 = START_SEQ + 6;
    public static final int MEAL_ID_6 = START_SEQ + 7;

    public static final Meal meal1 = new Meal(MEAL_ID_1,LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_ID_2,LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(MEAL_ID_3,LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(MEAL_ID_4,LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal5 = new Meal(MEAL_ID_5,LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal meal6 = new Meal(MEAL_ID_6,LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
