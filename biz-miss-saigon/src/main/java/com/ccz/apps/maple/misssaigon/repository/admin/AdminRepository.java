package com.ccz.apps.maple.misssaigon.repository.admin;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.admin.AdminCommonRepository;
import com.ccz.modules.repository.db.board.BoardCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminRepository extends AdminCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }

}
