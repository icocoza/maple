package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EUserRole {
    none("none"), user("user"), adminUser("adminUser"), adminMaster("adminMaster"), anonymous("anonymous");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EUserRole(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EUserRole(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EUserRole> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EUserRole cmd : EUserRole.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EUserRole getType(String cmd) {
        EUserRole ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
