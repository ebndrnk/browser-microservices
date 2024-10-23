package org.ebndrnk.springbrauser.controller;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.ebndrnk.springbrauser.service.util.PageParserService;
import org.ebndrnk.springbrauser.service.user_actions.SearchService;
import org.ebndrnk.springbrauser.service.user_actions.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexPageController {
    private final PageParserService service;
    private final SearchService searchService;
    private final UserService userService;
    private final LogService logService;

    @GetMapping()
    public String index(Model model) {
        Long userId = userService.getCurrentUserId();
        logService.logInfo("Accessing index page for user ID: " + userId);

        User user = userService.findUserById(userId);
        if (user.getUsername() != null) {
            model.addAttribute("name", user.getUsername());
            logService.logInfo("User found: " + user.getUsername());
            model.addAttribute("photoExist", user.getPhoto() != null);
        } else {
            model.addAttribute("name", "You need to authorized");
            model.addAttribute("photoExist", false);
            logService.logWarn("Unauthorized access attempt with user ID: " + userId);
        }
        return "indexPage";
    }

    @GetMapping("/settings")
    public String settings() {
        logService.logInfo("Accessing settings page");
        return "settings";
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("query") String query) {
        logService.logInfo("Performing search with query: " + query);
        searchService.saveSearchRequest(query);
        return service.getPage(query);
    }
}