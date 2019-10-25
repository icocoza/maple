package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EMessageCmd {
    msg("msg"), syncmsg("syncmsg"), rcvmsg("rcvmsg"), readmsg("readmsg"), delmsg("delmsg"), online("online"), push("push"), none("none");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EMessageCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EMessageCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EMessageCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EMessageCmd cmd : EMessageCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EMessageCmd getType(String cmd) {
        EMessageCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
