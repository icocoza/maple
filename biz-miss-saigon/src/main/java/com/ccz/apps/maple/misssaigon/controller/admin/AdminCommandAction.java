package com.ccz.apps.maple.misssaigon.controller.admin;

import com.ccz.apps.maple.misssaigon.repository.admin.AdminRepository;
import com.ccz.modules.controller.admin.AdminCommandCommonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AdminCommandAction extends AdminCommandCommonAction {
    @Autowired
    AdminRepository adminCommonRepository;

    @PostConstruct
    public void init() {
        super.initCommandFunctions(adminCommonRepository);
    }

}
