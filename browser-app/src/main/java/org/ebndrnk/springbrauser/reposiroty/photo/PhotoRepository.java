package org.ebndrnk.springbrauser.reposiroty.photo;

import org.ebndrnk.springbrauser.model.dao.Photo;
import org.ebndrnk.springbrauser.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Photo p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
