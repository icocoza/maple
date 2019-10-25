package com.ccz.apps.maple.misssaigon.controller.oauth;

import com.ccz.apps.maple.misssaigon.repository.user.UserRepository;
import com.ccz.modules.controller.oauth.OAuthCommandCommonAction;
import com.ccz.modules.controller.user.UserCommandCommonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OAuthCommandAction extends OAuthCommandCommonAction {

    @Autowired
    UserRepository userRepository;

    public OAuthCommandAction() {

    }

    @PostConstruct
    public void init() {
        super.initCommandFunctions(userRepository);

        //super.setCommandFunction(EUserCmd.signIn.name(), doSignin); re-define if need..
    }
}
