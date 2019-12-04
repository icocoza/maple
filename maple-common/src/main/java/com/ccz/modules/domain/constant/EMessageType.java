package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EMessageType {
    none("none"), chat("chat"), online("online"), push("push");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EMessageType(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EMessageType(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EMessageType> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EMessageType cmd : EMessageType.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EMessageType getType(String cmd) {
        EMessageType ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
