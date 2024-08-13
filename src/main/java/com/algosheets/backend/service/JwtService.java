package com.algosheets.backend.service;

import java.util.Map;
import java.util.UUID;

public interface JwtService {

    String generateToken(Map<String, Object> extraClaims, String userId);

    String generateToken(String userId);

    String extractUserId(String jwtToken);

    Boolean isTokenValid(String jwtToken);
}
