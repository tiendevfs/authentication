package com.example.authentication.Repository;

import com.example.authentication.Model.Entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
    boolean existsById(String id);
}
