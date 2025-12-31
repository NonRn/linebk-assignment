package com.linebk.assignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.linebk.assignment.models.entities.User;
import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT user_id FROM users LIMIT 50000", nativeQuery = true)
    List<String> getAllUserIdsLimitNative();

}
