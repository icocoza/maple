package com.ccz.modules.server.config;

import lombok.Data;

public class OAuthConfig {

    public static class GoogleOAuth{
        public String clientId;
        public String clientSecret;
        public String redirectUriTemplate;
        public String[] scope = {"email", "profile"};
    }


    public static class FacebookOAuth{
        public static String clientId = "500523974105110";
        public static String clientSecret = "31b0e2d540274e88300e23797cd9cc06";
        public static String redirectUriTemplate = "https://localhost:8443/oauth2/facebook";
        public static String[] scope = {"email", "public_profile"};
    }

    public static class GithubOAuth{
        public String clientId;
        public String clientSecret;
        public String redirectUriTemplate;
        public String[] scope = {"user:email", "read:user"};
    }

}
