package com.algosheets.backend.service;

import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.OAuthResponse;

public interface CommonService {


    Auth updateAuthDetails(Auth existingAuth, OAuthResponse oAuthResponse);
}
