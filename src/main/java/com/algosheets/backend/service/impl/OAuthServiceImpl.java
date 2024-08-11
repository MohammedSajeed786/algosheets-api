package com.algosheets.backend.service.impl;

import com.algosheets.backend.context.AuthContext;
import com.algosheets.backend.exception.HttpException;
import com.algosheets.backend.model.*;
import com.algosheets.backend.repository.AuthRepository;
import com.algosheets.backend.service.CommonService;
import com.algosheets.backend.service.OAuthService;
import com.algosheets.backend.utility.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.algosheets.backend.constants.AppConstants.POST_MESSAGE;
import static com.algosheets.backend.constants.HttpConstants.*;

@Service
public class OAuthServiceImpl implements OAuthService {

    @Autowired
    CommonService commonService;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    private HttpUtil httpUtil;
    @Value("${oauth.client.id}")
    private String clientId;
    @Value("${oauth.client.secret}")
    private String clientSecret;
    @Value("${oauth.redirect.uri}")
    private String redirectUri;

    @Override
    public OAuthResponse exchangeToken(String grantType, String token) {
        InitRequest<OAuthResponse> initRequest = new InitRequest<>();
        initRequest.setUrl(TOKEN_EXCHANGE_URL);
        initRequest.setResponseType(OAuthResponse.class);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        if (grantType.equals("authorization_code")) {
            body.add("code", token);
            body.add("redirect_uri", POST_MESSAGE);
        } else {
            body.add("refresh_token", token);
        }
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", grantType);

        initRequest.setBody(body);
        initRequest.setMethod(HttpMethod.POST);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        initRequest.setHeaders(headers);

        try {
            return (OAuthResponse) httpUtil.doHttpRequest(initRequest).getBody();
        }
         catch (HttpException e) {
            if (e.getStatusCode() == 401) {
                return refreshTokenAndRetryRequest(initRequest);
            } else throw e;
        }
    }


    @Override
    public UserProfile getUserDetails(String accessToken) {
        InitRequest<UserProfile> initRequest = new InitRequest<>();
        initRequest.setUrl(USER_PROFILE_URL);
        initRequest.setResponseType(UserProfile.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        initRequest.setHeaders(headers);
        initRequest.setMethod(HttpMethod.GET);
        try {
            return (UserProfile) httpUtil.doHttpRequest(initRequest).getBody();
        } catch (HttpException e) {
            if (e.getStatusCode() == 401) {
                return refreshTokenAndRetryRequest(initRequest);
            } else throw e;
        }
    }

    @Override
    public List<FileInfo> getAllFiles(String accessToken) {
        InitRequest<DriveFilesResponse> initRequest = new InitRequest<>();
        initRequest.setUrl(FOLDER_ACCESS_URL);
        initRequest.setResponseType(DriveFilesResponse.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        initRequest.setHeaders(headers);
        initRequest.setMethod(HttpMethod.GET);
        try {
            return ((DriveFilesResponse) httpUtil.doHttpRequest(initRequest).getBody()).getFiles();
        } catch (HttpException e) {
            if (e.getStatusCode() == 401) {
                return refreshTokenAndRetryRequest(initRequest).getFiles();
            } else throw e;
        }

    }

    @Override
    public OAuthResponse refreshAccessToken(String refreshToken) {
        return exchangeToken("refresh_token", refreshToken);
    }

    @Override
    public String getFileContent(String fileId, String accessToken) {
        InitRequest<String> initRequest = new InitRequest<>();
        initRequest.setUrl(FILE_CONTENT_URL.replace("{fileId}", fileId));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        initRequest.setHeaders(headers);
        initRequest.setMethod(HttpMethod.GET);
        initRequest.setResponseType(String.class);
        try {
          return httpUtil.doHttpRequest(initRequest).getBody();
        } catch (HttpException e) {
            if (e.getStatusCode() == 401) {
                return (refreshTokenAndRetryRequest(initRequest));
            } else throw e;
        }
    }


    private <T> T refreshTokenAndRetryRequest(InitRequest<T> initRequest){
        Auth auth = authRepository.findByUserId(AuthContext.getContext()).get();
        String accessToken = commonService.updateAuthDetails(auth, refreshAccessToken(auth.getRefreshToken())).getAccessToken();
        Map<String, String> headers=initRequest.getHeaders();
        headers.put("Authorization", "Bearer " + accessToken);
        initRequest.setHeaders(headers);
        return httpUtil.doHttpRequest(initRequest).getBody();
    }

    @Override
    public void UpdateFileContent(String fileId, String fileContent, String accessToken){
        InitRequest<String> initRequest = new InitRequest<>();
        initRequest.setUrl(UPDATE_FILE_URL.replace("{fileId}", fileId));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        initRequest.setHeaders(headers);
        initRequest.setMethod(HttpMethod.PUT);
        initRequest.setBody(fileContent);
        initRequest.setResponseType(String.class);
        try {
             httpUtil.doHttpRequest(initRequest).getBody();
        } catch (HttpException e) {
            if (e.getStatusCode() == 401) {
                 refreshTokenAndRetryRequest(initRequest);
            } else throw e;
        }
    }
}
