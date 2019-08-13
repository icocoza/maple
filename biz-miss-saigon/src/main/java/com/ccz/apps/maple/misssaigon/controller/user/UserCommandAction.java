package com.ccz.apps.maple.misssaigon.controller.user;

import com.ccz.apps.maple.misssaigon.repository.user.UserRepository;
import com.ccz.modules.controller.user.UserCommandCommonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserCommandAction extends UserCommandCommonAction {

    @Autowired
    UserRepository userRepository;

    public UserCommandAction() {

    }

    @PostConstruct
    public void init() {
        super.initCommandFunctions(userRepository);

        //super.setCommandFunction(EUserCmd.signIn.name(), doSignin); re-define if need..
    }
}
