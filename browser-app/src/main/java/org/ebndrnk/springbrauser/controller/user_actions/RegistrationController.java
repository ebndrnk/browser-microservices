package org.ebndrnk.springbrauser.controller.user_actions;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.service.user_actions.UserService;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final LogService logService;

    @GetMapping("/registration")
    public String registration(Model model) {
        logService.logInfo("Accessing registration page");
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm,
                          BindingResult bindingResult, Model model) {
        logService.logInfo("Attempting to register user: " + userForm.getUsername());

        if (bindingResult.hasErrors()) {
            logService.logWarn("Validation errors occurred during registration");
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            logService.logWarn("Passwords do not match for user: " + userForm.getUsername());
            return "registration";
        }
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            logService.logWarn("Username already exists: " + userForm.getUsername());
            return "registration";
        }

        logService.logInfo("User registered successfully: " + userForm.getUsername());
        return "forward:/login";
    }

    @GetMapping("/auth")
    public String auth() {
        try {
            if (!userService.findUserById(userService.getCurrentUserId()).getUsername().isEmpty()) {
                logService.logInfo("User authenticated, redirecting to user page");
                return "forward:/user-page";
            }
        } catch (Exception e) {
            logService.logWarn("Unauthorized user: " + e.getMessage());
        }
        logService.logInfo("Redirecting to login page for unauthorized access");
        return "LoR";
    }

    @GetMapping("")
    public String redirect() {
        logService.logInfo("Redirecting to index page");
        return "forward:/index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        logService.logInfo("Accessing login page");
        model.addAttribute("userForm", new User());
        if (error != null) {
            model.addAttribute("loginError", true);
            logService.logWarn("Login error occurred");
        }
        return "login";
    }
}