package com.algosheets.backend.repository;

import com.algosheets.backend.model.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends MongoRepository<Auth, UUID> {
    Optional<Auth> findByEmail(String email);

    Optional<Auth> findByUserId(String userId);

}
