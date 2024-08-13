package com.algosheets.backend.service.impl;

import com.algosheets.backend.context.AuthContext;
import com.algosheets.backend.dto.CodeDTO;
import com.algosheets.backend.exception.DaoException;
import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.OAuthResponse;
import com.algosheets.backend.model.UserProfile;
import com.algosheets.backend.repository.AuthRepository;
import com.algosheets.backend.service.AuthService;
import com.algosheets.backend.service.CommonService;
import com.algosheets.backend.service.JwtService;
import com.algosheets.backend.service.OAuthService;
import com.algosheets.backend.utility.OAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private OAuthUtil oAuthUtil;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private CommonService commonService;

    @Override
    public String handleOAuthCallback(CodeDTO code) {
        OAuthResponse response = oAuthService.exchangeToken("authorization_code", code.getCode());
        UserProfile userProfile = oAuthService.getUserDetails(response.getAccess_token());
        String email=userProfile.getEmailAddresses().get(0).getValue();

        Auth auth=null;
        Optional<Auth> optionalAuth=authRepository.findByEmail(email);

        //check if user already exists with this email
        if(optionalAuth.isPresent()) {
            auth = optionalAuth.get();
            auth = oAuthUtil.modifyAuth(auth,response);
        }
        else {
            auth = oAuthUtil.convertToAuth(response);
            auth.setEmail(email);
        }

        auth = authRepository.save(auth);

        return generateJwtToken(userProfile, auth.getUserId());
    }

    private String generateJwtToken(UserProfile userProfile, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userProfile.getEmailAddresses().get(0).getValue());
        claims.put("name", userProfile.getNames().get(0).getDisplayName());
        claims.put("profilePicture", userProfile.getPhotos().get(0).getUrl());
        return jwtService.generateToken(claims, userId);
    }

    @Override
    public String refreshTokenAndGenerateJwt(String email) {
        Optional<Auth> authOptional = authRepository.findByEmail(email);
        if (!authOptional.isPresent()) {
            throw new DaoException("Email not found in database");
        }

        Auth auth = authOptional.get();
        if (auth.getAccessToken() == null || auth.getTokenExpiry().isBefore(LocalDateTime.now()))
            auth = refreshTokenAndUpdateAuth(auth);


        UserProfile userProfile = oAuthService.getUserDetails(auth.getAccessToken());
        return generateJwtToken(userProfile, auth.getUserId());
    }

    @Override
    public Auth refreshTokenAndUpdateAuth(Auth auth) {
        OAuthResponse response = oAuthService.refreshAccessToken(auth.getRefreshToken());
        return commonService.updateAuthDetails(auth,response);
    }

    @Override
    public boolean isEmailExists(String email) {
        return authRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<Auth> findAuthByUserId(String uuid) {
        return authRepository.findByUserId(uuid);
    }

    @Override
    public String getAccessToken(){
        String userId=AuthContext.getContext();
        return authRepository.findByUserId(userId).get().getAccessToken();
    }
}
