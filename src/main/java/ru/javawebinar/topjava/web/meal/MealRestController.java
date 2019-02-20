package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());


    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getBetween(String sd, String ed, String st, String et) {
        log.info("filtered getBetween");
        LocalDate startDate = LocalDate.of(0,1,1);
        LocalDate endDate = LocalDate.of(3000,1,1);
        LocalTime startTime = LocalTime.MIN;
        LocalTime endTime = LocalTime.MAX;
        if (!sd.isEmpty()) startDate = LocalDate.parse(sd);
        if (!ed.isEmpty()) endDate = LocalDate.parse(ed);
        if (!st.isEmpty()) startTime = LocalTime.parse(st);
        if (!et.isEmpty()) endTime = LocalTime.parse(et);


        return MealsUtil.getFilteredWithExcessBetweenDateAndTime(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), startDate, startTime, endDate, endTime);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {} for user {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} with id={} for user {}", meal, id, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }


}