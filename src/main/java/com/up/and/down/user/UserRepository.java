package com.up.and.down.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
        select
            u
        from
            User u join fetch u.authorities
        where
            u.userId = :username
        """)
    Optional<User> findByUsername(String username);
}
