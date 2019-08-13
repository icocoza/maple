package com.ccz.apps.maple.misssaigon.repository.board;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.board.BoardCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardRepository extends BoardCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    @Override
    public String getPoolName() {
        return missSaigonConfig.getPoolname();
    }

}
