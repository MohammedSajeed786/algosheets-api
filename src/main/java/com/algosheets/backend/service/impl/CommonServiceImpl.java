package com.algosheets.backend.service.impl;

import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.OAuthResponse;
import com.algosheets.backend.repository.AuthRepository;
import com.algosheets.backend.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class CommonServiceImpl implements CommonService {

    @Autowired
    AuthRepository authRepository;

    @Override
    public Auth updateAuthDetails(Auth existingAuth, OAuthResponse oAuthResponse){
        existingAuth.setAccessToken(oAuthResponse.getAccess_token());
        existingAuth.setTokenExpiry(LocalDateTime.now().plusSeconds(oAuthResponse.getExpires_in()));
        return authRepository.save(existingAuth);
    }
}
