package com.ccz.apps.maple.misssaigon;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.apps.maple.misssaigon.controller.file.FileCommandAction;
import com.ccz.apps.maple.misssaigon.controller.user.UserCommandAction;
import com.ccz.modules.common.dbhelper.DbConnMgr;
import com.ccz.modules.domain.inf.IBizMain;
import com.ccz.modules.domain.inf.ICommandFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MissSaigonMain implements IBizMain {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    @Autowired
    UserCommandAction userCommandAction;

    @Autowired
    FileCommandAction fileCommandAction;

    @Override
    public void init() {
        try {
            DbConnMgr.getInst().createConnectionPool(missSaigonConfig.getPoolname(), missSaigonConfig.getMysqlUrl(), missSaigonConfig.getMysqlUsername(), missSaigonConfig.getMysqlPassword(), 4, 8);
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Map<String, ICommandFunction> getCommandFunctions() {
        Map<String, ICommandFunction> cmdMap = new ConcurrentHashMap<>();
        cmdMap.putAll(userCommandAction.getCommandFunctions());
        cmdMap.putAll(fileCommandAction.getCommandFunctions());
        return cmdMap;
    }


}
