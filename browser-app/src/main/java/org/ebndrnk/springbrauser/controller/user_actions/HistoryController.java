package org.ebndrnk.springbrauser.controller.user_actions;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.service.user_actions.SearchService;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user-page")
@RequiredArgsConstructor
public class HistoryController {
    private final SearchService service;
    private final LogService logService;

    @PostMapping("/delete-history")
    public String deleteHistory() {
        logService.logInfo("Attempting to delete all history");
        service.deleteAllHistory();
        logService.logInfo("All history deleted successfully");
        return "forward:/user-page";
    }

    @PostMapping("/delete-request/{id}")
    public String deleteRequest(@PathVariable int id) {
        logService.logInfo("Attempting to delete search request with ID: " + id);
        service.deleteSearchRequest(id);
        logService.logInfo("Search request with ID " + id + " deleted successfully");
        return "forward:/user-page";
    }
}