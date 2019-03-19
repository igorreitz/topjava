package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private final MealRestController service;

    public JspMealController(MealRestController service) {
        this.service = service;
    }

    //show universal meal list (with filtering)
    @GetMapping
    public String meals(HttpServletRequest request,Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", service.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    //delete meal
    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {
        service.delete(Integer.valueOf(request.getParameter("userId")));
        return "redirect:/meals";
    }

    //save or update meal
    @PostMapping("**/save")
    public String update(@ModelAttribute("mealForm") Meal meal) {
        if (StringUtils.isEmpty(meal.getId()))
            service.create(meal);
        else
            service.update(meal, meal.getId());

        return "redirect:/meals";
    }

    // show update form
    @GetMapping(value = "/{id}/update")
    public String showUpdateUserForm(@PathVariable("id") int id, Model model) {
        Meal meal = service.get(id);
        model.addAttribute("mealForm", meal);

        return "mealForm";
    }

    //show add form
    @GetMapping(value = "/add")
    public String showAddUserForm(Model model) {
        Meal meal = new Meal(LocalDateTime.now(),"",500);
        model.addAttribute("mealForm", meal);

        return "mealForm";
    }
}
