package com.pm.ex8.repository;

import com.pm.ex8.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        select u from users u where u.username = :username
    """)
    Optional<User> findByUsername(@Param("username") String username);
}
