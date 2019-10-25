package com.ccz.apps.maple.misssaigon.repository.user;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepository extends UserCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    private UserRepository() {
    }

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }
}
