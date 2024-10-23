package org.ebndrnk.springbrauser.reposiroty.user;

import org.ebndrnk.springbrauser.model.dao.SearchRequest;
import org.ebndrnk.springbrauser.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {
    List<SearchRequest> findByUser(User user);
}
