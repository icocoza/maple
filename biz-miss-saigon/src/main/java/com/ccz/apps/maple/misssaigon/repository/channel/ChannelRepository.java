package com.ccz.apps.maple.misssaigon.repository.channel;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.board.BoardCommonRepository;
import com.ccz.modules.repository.db.channel.ChannelCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelRepository extends ChannelCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }

}
