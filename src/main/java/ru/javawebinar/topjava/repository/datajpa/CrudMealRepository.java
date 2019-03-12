package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id")int id, @Param("userId") int userId);

    @Override
    Meal save(Meal meal);

    @Query("SELECT m FROM Meal m WHERE m.user.id = ?3 AND m.dateTime between ?1 and ?2 ORDER BY m.dateTime desc ")
    List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);

    @Query("SELECT m FROM Meal m where m.user.id = ?1 ORDER BY m.dateTime desc")
    List<Meal> getAll(int userId);

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    Meal get(@Param("id")int id, @Param("userId") int userId);
}

