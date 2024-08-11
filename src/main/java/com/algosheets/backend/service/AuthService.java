package com.algosheets.backend.service;

import com.algosheets.backend.dto.CodeDTO;
import com.algosheets.backend.model.Auth;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {

    String handleOAuthCallback(CodeDTO code);

    String refreshTokenAndGenerateJwt(String email);

    Auth refreshTokenAndUpdateAuth(Auth auth);

    boolean isEmailExists(String email);

    Optional<Auth> findAuthByUserId(UUID uuid);

    String getAccessToken();
}
