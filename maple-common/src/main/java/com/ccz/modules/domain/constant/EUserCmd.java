package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EUserCmd {

    regIdPw("regIdPw"), regEmail("regEmail"), regPhone("regPhone"), login("login"), signIn("signIn"),
    changePw("changePw"), updateEmail("updateEmail"), updatePhone("updatePhone"), verifyEmail("verifyEmail"), verifySms("verifySms"),
    anonymousLogin("anonymousLogin"), anonymousSignin("anonymousSignin"), findId("findId"), none("none"),

    oAuthFaebookJoin("oAuthFaebookJoin"), oAuthFaebookSignIn("oAuthFaebookSignIn");

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
