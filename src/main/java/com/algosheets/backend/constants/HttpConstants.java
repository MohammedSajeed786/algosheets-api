package com.algosheets.backend.constants;

public class HttpConstants {
    public static final String TOKEN_EXCHANGE_URL = "https://oauth2.googleapis.com/token";
    public static final String USER_PROFILE_URL = "https://people.googleapis.com/v1/people/me?personFields=emailAddresses,names,photos";
    public static final String FOLDER_ACCESS_URL = "https://www.googleapis.com/drive/v3/files?q='1pOKrlLDG1_P550wmKe__NAsCIFaa_New' in parents";
    public static final String FILE_URL = "https://www.googleapis.com/drive/v3/files/{fileId}";
    public static final String FILE_CONTENT_URL = "https://www.googleapis.com/drive/v3/files/{fileId}?alt=media";
    public static final String UPDATE_FILE_URL = "https://www.googleapis.com/upload/drive/v2/files/{fileId}?uploadType=media";


}
