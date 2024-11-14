package vttp.batch5.ssf.Day13NoHttpSession.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import vttp.batch5.ssf.Day13NoHttpSession.model.Task;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("tasks", tasks);
        model.addAttribute("isConflicting", false);
        return "index";
    }

    @PostMapping("/checkAvailability")
    public String checkAvailability(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tasks", tasks);
            return "index";
        }

        boolean isConflicting = tasks.stream()
                .anyMatch(existingTask -> existingTask.getDueDate().isEqual(task.getDueDate()));

        model.addAttribute("isConflicting", isConflicting);
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", task);

        if (!isConflicting) {
            model.addAttribute("availabilityMessage", "Looks like you are free!");
        } else {
            model.addAttribute("conflictMessage", "There is already a task scheduled for this date.");
        }

        return "index";
    }

    @PostMapping("/saveTask")
    public String saveTask(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tasks", tasks);
            return "index";
        }

        tasks.add(task);
        model.addAttribute("successMessage", "Task added successfully.");
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());

        return "index";
    }
}
