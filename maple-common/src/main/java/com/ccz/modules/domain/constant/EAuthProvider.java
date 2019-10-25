package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EAuthProvider {
    none("none"), local("local"), facebook("facebook"), google("google"), github("github");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EAuthProvider(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EAuthProvider(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EAuthProvider> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EAuthProvider cmd : EAuthProvider.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EAuthProvider getType(String cmd) {
        EAuthProvider ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
