package org.ebndrnk.springbrauser.reposiroty.user;


import org.ebndrnk.springbrauser.model.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
