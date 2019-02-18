package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

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

    public List<MealTo> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("filtered getAll");
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        if (startTime == null) startTime = LocalTime.MIN;
        if (endTime == null) endTime = LocalTime.MAX;

        return MealsUtil.getFilteredWithExcess(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(),startDate,startTime,endDate,endTime);
    }

    public MealTo get(int id) {
        log.info("get {}", id);
        return getAll().stream()
                .filter(mealTo -> mealTo.getId() == service.get(id,SecurityUtil.authUserId()).getId())
                .findFirst()
                .get();
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal,SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id,SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal,SecurityUtil.authUserId());
    }



}