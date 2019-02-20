package ru.javawebinar.topjava.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class MealServlet extends HttpServlet {
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);

    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (startDate != null || endDate != null || startTime != null || endTime != null) {
            request.setAttribute("meals", mealRestController.getAll(startDate, endDate, startTime, endTime));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }

        String userId = request.getParameter("userId");
        if (userId != null) {
            SecurityUtil.setAuthUserId(Integer.valueOf(userId));
            request.setAttribute("meals", mealRestController.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }



        String id = request.getParameter("id");
        String dateTime = request.getParameter("dateTime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");

        if (dateTime != null && description != null && calories != null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id), LocalDateTime.parse(dateTime),
                    description, Integer.parseInt(calories), SecurityUtil.authUserId());

            if (meal.isNew())
                mealRestController.create(meal);
            else
                mealRestController.update(meal, meal.getId());

            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                mealRestController.delete(getId(request));
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
