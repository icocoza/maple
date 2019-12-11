package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EUserCmd {

    registerIdPw("registerIdPw"), registerEmail("registerEmail"), registerPhone("registerPhone"), userLogin("userLogin"), userSignIn("userSignIn"),
    userChangePW("userChangePW"), userChangeEmail("userChangeEmail"), userChangePhone("userChangePhone"), userVerifyEmail("userVerifyEmail"), userVerifySms("userVerifySms"),
    anonymousLogin("anonymousLogin"), anonymousSignIn("anonymousSignIn"), userFindId("userFindId"), none("none");

    @Getter private final String value;
    @Getter private final boolean needSession;

    private EUserCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EUserCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EUserCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EUserCmd cmd : EUserCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EUserCmd getType(String cmd) {
        EUserCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
