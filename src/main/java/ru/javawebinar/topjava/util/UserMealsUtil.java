package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        //getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    //hw-0 by cycle
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerData = new HashMap<>();
        mealList.forEach(meal -> caloriesPerData.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        List<UserMealWithExceed> result = new ArrayList<>();
        for (UserMeal meal : mealList) {
            LocalDateTime mealDateTime = meal.getDateTime();
            LocalDate mealDate = mealDateTime.toLocalDate();
            LocalTime mealTime = mealDateTime.toLocalTime();
            boolean exceed = caloriesPerData.get(mealDate) > caloriesPerDay;

            if (TimeUtil.isBetween(mealTime, startTime, endTime))
                result.add(new UserMealWithExceed(mealDateTime, meal.getDescription(), meal.getCalories(), exceed));
        }
        return result;
    }

    //hw-0 optional by stream
    public static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = mealList
                .stream()
                .collect(Collectors.groupingBy(s -> s.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        return mealList
                .stream()
                .filter(s -> TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime))
                .map(s -> new UserMealWithExceed(s.getDateTime(), s.getDescription(), s.getCalories(), caloriesPerDate.get(s.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    //hw-0 optional2 by stream
    public static List<UserMealWithExceed> getFilteredWithExceededByStreamOptional2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList
                .stream()
                .collect(Collectors.groupingBy(UserMeal::getDate))
                .values()
                .stream()
                .flatMap(userMealListPerDate -> {
                    boolean exceed = userMealListPerDate
                            .stream()
                            .mapToInt(UserMeal::getCalories)
                            .sum() > caloriesPerDay;
                    return userMealListPerDate
                            .stream()
                            .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                            .map(meal -> getNewUserMealWithExceed(meal, exceed));
                }).collect(Collectors.toList());
    }

    private static UserMealWithExceed getNewUserMealWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), exceed);
    }
}
