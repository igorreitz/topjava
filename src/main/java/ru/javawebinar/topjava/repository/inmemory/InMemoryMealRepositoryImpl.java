package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.getUserId() == userId) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                repository.put(meal.getId(), meal);
                return meal;
            }
            // treat case: update, but absent in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else
            return null;
    }

    @Override
    public boolean delete(int mealId, int userId) {
        try {
            Meal meal = repository.get(mealId);
            if (meal.getUserId() == userId) {
                repository.remove(mealId);
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }

    }

    @Override
    public Meal get(int mealId, int userId) {
        try {
            Meal meal = repository.get(mealId);
            if (meal.getUserId() == userId) {
                return meal;
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDate)
                        .thenComparing(Meal::getTime)
                        .reversed())
                .collect(Collectors.toList());
    }
}

