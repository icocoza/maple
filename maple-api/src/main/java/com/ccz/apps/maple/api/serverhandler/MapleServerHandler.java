package com.ccz.apps.maple.api.serverhandler;

import com.ccz.apps.maple.misssaigon.MissSaigonMain;
import com.ccz.modules.common.action.ChAttributeKey;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.inf.IServiceActionHandler;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.server.service.DbScodeManager;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MapleServerHandler implements IServiceActionHandler {

    @Autowired
    MissSaigonMain missSaigonMain;

    private Map<String, ICommandFunction> cmdFuncMap = new ConcurrentHashMap<>();

    @Override
    public void start() {
        missSaigonMain.init();

        if(cmdFuncMap.size()<1) {
            cmdFuncMap.putAll(missSaigonMain.getCommandFunctions());
        }
    }

    @Override
    public String process(Channel ch, String msg) {
        log.info(msg);
        return "";
    }

    @Override
    public String process(Channel ch, JsonNode jNode) {
        return "";
    }

    @Override
    public String process(Channel ch, CommonForm form) {
        AuthSession authSession = null;
        if(ch != null) {
            authSession = ch.attr(ChAttributeKey.getAuthSessionKey()).get();
            if(authSession == null) {
                authSession = new AuthSession(ch);
            }
        }

        ResponseData<EAllError> res = new ResponseData<EAllError>(form.getScode(), form.getCmd());

//        try {
//            if(DbScodeManager.getInst().initDbForServiceCode(form.getScode()) == false) {
//                res.setError(EAllError.not_exist_scode).setParam("scode", form.getScode());
//                return res.toJsonString();
//            }
//        }catch (Exception e) {
//            res.setError(EAllError.unknown_error).setParam("result", e.getMessage());
//            return res.toJsonString();
//        }

        ICommandFunction func = cmdFuncMap.get(form.getCmd());
        if(func == null) {
            return res.setError(EAllError.eNoServiceCommand).setParam("result", "NoServiceCommand").toJsonString();
        }
        res = (ResponseData<EAllError>)func.doAction(authSession, res, form);
        return res.toJsonString();
    }

    @Override
    public void onClose(Channel ch) {

    }

}
