package com.algosheets.backend.utility;

import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.OAuthResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OAuthUtil {

    public Auth convertToAuth(OAuthResponse oAuthResponse) {
        return Auth.builder()
                .accessToken(oAuthResponse.getAccess_token())
                .refreshToken(oAuthResponse.getRefresh_token())
                .tokenExpiry(LocalDateTime.now().plusSeconds(oAuthResponse.getExpires_in()))
                .build();
    }

    public Auth modifyAuth(Auth auth, OAuthResponse oAuthResponse) {
        auth.setAccessToken(oAuthResponse.getAccess_token());
        auth.setRefreshToken(oAuthResponse.getRefresh_token());
        auth.setTokenExpiry(LocalDateTime.now().plusSeconds(oAuthResponse.getExpires_in()));
        return auth;
    }

}
