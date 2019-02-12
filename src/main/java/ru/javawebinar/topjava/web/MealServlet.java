package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String LIST_USER = "/meals.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = "";
       // String action = req.getParameter("action");


            forward = LIST_USER;
            req.setAttribute("meals", MealsUtil.getFilteredWithExcess(MealsUtil.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));


        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }
}
