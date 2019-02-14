package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            log.info("GetAll");
            req.setAttribute("mealList", MealsUtil.getWithExcess(repository.getAll(), 2000));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("delete")) {
            int mealId = getId(req);
            log.info("Delete {}", mealId);
            repository.delete(mealId);
            resp.sendRedirect("meals");
        } else {
            log.info(action);
            final Meal meal = action.equals("create") ?
                    new Meal(LocalDateTime.now(), "", 0) :
                    repository.get(getId(req));

            req.setAttribute("meal", meal);
            req.getRequestDispatcher("edit.jsp").forward(req, resp);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("mealId"));
        return Integer.valueOf(paramId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String mealId = req.getParameter("mealId");
        Meal meal = new Meal(mealId.isEmpty() ? null : Integer.valueOf(mealId),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.valueOf(req.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal);
        resp.sendRedirect("meals");
    }
}