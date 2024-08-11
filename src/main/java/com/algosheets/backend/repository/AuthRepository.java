package com.algosheets.backend.repository;

import com.algosheets.backend.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    Optional<Auth> findByEmail(String email);

    Optional<Auth> findByUserId(UUID userId);

}
