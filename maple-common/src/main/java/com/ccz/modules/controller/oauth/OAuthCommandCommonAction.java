package com.ccz.modules.controller.oauth;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.controller.oauth.OAuthForm.*;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.constant.EUserCmd;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import com.ccz.modules.server.config.OAuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OAuthCommandCommonAction extends CommandAction {

    UserCommonRepository userCommonRepository;

    FacebookConnectionFactory facebookConnectionFactory;
    OAuth2Parameters oAuth2Parameters;


    //TODO OAuthConfig.FacebookOAuth.clientId static 지시자 제거해야 함. 하위 인스턴스에서 결정할 수 있도록 변경 필요
    public OAuthCommandCommonAction() {
        facebookConnectionFactory = new FacebookConnectionFactory(OAuthConfig.FacebookOAuth.clientId, OAuthConfig.FacebookOAuth.clientSecret);

        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("scope", Arrays.asList(OAuthConfig.FacebookOAuth.scope));
        parameters.put("redirectUri", Arrays.asList(OAuthConfig.FacebookOAuth.redirectUriTemplate));
        oAuth2Parameters = new OAuth2Parameters(parameters);
    }

    @Override
    public void initCommandFunctions(CommonRepository userCommonRepository) {
        this.userCommonRepository = (UserCommonRepository)userCommonRepository;
        super.setCommandFunction(makeCommandId(EUserCmd.oAuthFaebookJoin), doOAuthFacebookJoin);
        super.setCommandFunction(makeCommandId(EUserCmd.oAuthFaebookSignIn), doOAuthFacebookSignIn);
    }

    @Override
    public String makeCommandId(Enum e) {
        return "" + e.name();
    }



    ICommandFunction<AuthSession, ResponseData<EAllError>, JoinForm> doOAuthFacebookJoin = (AuthSession authSession, ResponseData<EAllError> res, JoinForm form) -> {
        OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
        String facebook_url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
        log.info("facebook_url: " + facebook_url);
        return res.setError(EAllError.ok).setParam(facebook_url);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, SignInForm> doOAuthFacebookSignIn = (AuthSession authSession, ResponseData<EAllError> res, SignInForm form) -> {
        String redirectUri = oAuth2Parameters.getRedirectUri();
        log.info("Redirect URI : " + redirectUri);
        log.info("Code : " + form.getCode());

        OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
        AccessGrant accessGrant = oauthOperations.exchangeForAccess(form.getCode(), redirectUri, null);
        String accessToken = accessGrant.getAccessToken();
        log.info("AccessToken: " + accessToken);
        Long expireTime = accessGrant.getExpireTime();


        if (expireTime != null && expireTime < System.currentTimeMillis()) {
            accessToken = accessGrant.getRefreshToken();
            log.info("accessToken is expired. refresh token = {}", accessToken);
        };

        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        Facebook facebook = connection == null ? new FacebookTemplate(accessToken) : connection.getApi();
        UserOperations userOperations = facebook.userOperations();

        try

        {
            String [] fields = { "id", "email",  "name"};
            User userProfile = facebook.fetchObject("me", User.class, fields);
            log.info("유저이메일 : " + userProfile.getEmail());
            log.info("유저 id : " + userProfile.getId());
            log.info("유저 name : " + userProfile.getName());
            return res.setError(EAllError.ok).setParam("userProfile", userProfile);
        } catch (MissingAuthorizationException e) {
            log.error(e.getMessage());
        }
        return res.setError(EAllError.unknown_error);
    };

}
