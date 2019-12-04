package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EFriendStatus {
    none("none"), normal("normal"), busy("busy"), offline("offline"), leave("leave");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EFriendStatus(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EFriendStatus(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EFriendStatus> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EFriendStatus cmd : EFriendStatus.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EFriendStatus getType(String cmd) {
        EFriendStatus ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
