package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EAdminAppStatus {
    none("none"), ready("ready"), run("run"), stop("stop"), delete("delete"), all("all");

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

    public String getValue() {
        return value;
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
