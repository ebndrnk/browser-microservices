package org.ebndrnk.springbrauser.service.user_actions;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.SearchRequest;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.reposiroty.user.SearchRequestRepository;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRequestRepository repository;
    private final UserService userService;
    private final LogService logService;

    public void saveSearchRequest(String request) {
        try {
            User currentUser = userService.findUserById(userService.getCurrentUserId());
            logService.logInfo("Creating a search request to saving to db");
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setRequest(request);
            searchRequest.setTimestamp(LocalDateTime.now());
            searchRequest.setUser(currentUser);
            logService.logInfo("trying to save search request to db");
            repository.save(searchRequest);
            logService.logInfo("search request saved");
        }catch (Exception e){
            logService.logWarn("Need to authorize");
        }
    }

    public List<SearchRequest> getSearchHistory() {
        User currentUser = userService.findUserById(userService.getCurrentUserId());
        return repository.findByUser(currentUser);
    }

    public void deleteSearchRequest(long id){
        repository.deleteById(id);
    }

    public void deleteAllHistory(){
        repository.deleteAll();
    }
}
