package com.linebk.assignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linebk.assignment.models.entities.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {

}
