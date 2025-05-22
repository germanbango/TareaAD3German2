package com.german.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.german.tarea3AD2024base.modelo.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
}
