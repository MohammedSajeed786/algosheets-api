package com.algosheets.backend.config.interceptors;

import com.algosheets.backend.context.AuthContext;
import com.algosheets.backend.exception.TokenException;
import com.algosheets.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getMethod().equals("OPTIONS")) {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new TokenException("Invalid authorization header");
            }

            String token = authorizationHeader.substring(7);

            try {


                if (jwtService.isTokenValid(token)) {
                    // Extract userId from the token and validate it
                    String userIdString = jwtService.extractUserId(token);
                    UUID userId = UUID.fromString(userIdString);
                    AuthContext.setContext(userId);
                    return true;
                } else {
                    throw new TokenException("Invalid token");
                }

            } catch (Exception e) {
                // Log the exception with details for debugging
                String errorMessage = String.format("Access denied: %s", e.getMessage());
                throw new TokenException(errorMessage);
            }
        } else return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        AuthContext.clearContext();
    }
}
