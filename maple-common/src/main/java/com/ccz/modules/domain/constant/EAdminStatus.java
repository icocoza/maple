package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EAdminStatus {
    none("none"), normal("normal"), pending("pending"), block("block"), leave("leave");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EAdminStatus(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EAdminStatus(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EAdminStatus> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EAdminStatus cmd : EAdminStatus.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EAdminStatus getType(String cmd) {
        EAdminStatus ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
