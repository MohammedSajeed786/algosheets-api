package com.algosheets.backend.service;

import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.OAuthResponse;
import com.algosheets.backend.model.UserProfile;

import java.util.List;

public interface OAuthService {
    OAuthResponse exchangeToken(String grantType, String token);
    UserProfile getUserDetails(String accessToken);

    List<FileInfo> getAllFiles(String accessToken);

    OAuthResponse refreshAccessToken(String refreshToken);

    String getFileContent(String fileId, String accessToken);

    void UpdateFileContent(String fileId, String fileContent, String accessToken);
}
