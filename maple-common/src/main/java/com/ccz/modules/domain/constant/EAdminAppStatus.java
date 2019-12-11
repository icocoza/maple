package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EAdminAppStatus {
    none("none"), READY("READY"), BLOCK("BLOCK"), DELETED("DELETED"), PENDING("PENDING"), STOP("STOP"), ALL("ALL");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EAdminAppStatus(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EAdminAppStatus(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EAdminAppStatus> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EAdminAppStatus cmd : EAdminAppStatus.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EAdminAppStatus getType(String cmd) {
        EAdminAppStatus ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
