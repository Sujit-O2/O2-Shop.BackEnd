package com.Ecom.Ecom.Repo;

import com.Ecom.Ecom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String username);

    User findByName(String username);
}
