package com.ccz.apps.maple.misssaigon.repository.message;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.board.BoardCommonRepository;
import com.ccz.modules.repository.db.message.MessageCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageRepository extends MessageCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }

}
