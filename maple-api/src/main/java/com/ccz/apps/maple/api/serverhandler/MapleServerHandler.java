package com.ccz.apps.maple.api.serverhandler;

import com.ccz.apps.maple.misssaigon.MissSaigonMain;
import com.ccz.modules.common.action.ChAttributeKey;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.inf.IServiceActionHandler;
import com.ccz.modules.domain.session.AuthSession;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
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
        String serviceCmd = getServiceCommand(form);
        ICommandFunction func = cmdFuncMap.get(serviceCmd);
        if(func == null) {
            return "Unknown Command";
        }
        res = (ResponseData<EAllError>)func.doAction(authSession, res, form);
        return res.toJsonString();
    }

    @Override
    public void onClose(Channel ch) {

    }

    private String getServiceCommand(CommonForm form) {
        return form.getScode() +":"+ form.getCmd();
    }
}
