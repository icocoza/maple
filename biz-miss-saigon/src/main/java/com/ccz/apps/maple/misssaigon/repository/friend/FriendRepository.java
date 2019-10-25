package com.ccz.apps.maple.misssaigon.repository.friend;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.repository.db.board.BoardCommonRepository;
import com.ccz.modules.repository.db.friend.FriendCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendRepository extends FriendCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }

}
