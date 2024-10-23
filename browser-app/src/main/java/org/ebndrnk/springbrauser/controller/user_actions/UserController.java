package org.ebndrnk.springbrauser.controller.user_actions;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.service.user_actions.SearchService;
import org.ebndrnk.springbrauser.service.user_actions.UserService;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final SearchService searchService;
    private final UserService userService;
    private final LogService logService;

    @GetMapping("/user-page")
    public String getUserPage(Model model) {
        logService.logInfo("Accessing user page");

        try {
            User user = userService.findUserById(userService.getCurrentUserId());
            model.addAttribute("user", user);
            model.addAttribute("history", searchService.getSearchHistory());
            model.addAttribute("photoExist", user.photoExist());
            logService.logInfo("User data loaded successfully for user: " + user.getUsername());
        } catch (Exception e) {
            logService.logError("Error loading user data: " + e.getMessage());
            return "forward:/index";
        }

        return "userPage";
    }

    // some admins controllers for actions with users
}