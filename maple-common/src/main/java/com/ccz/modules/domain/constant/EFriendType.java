package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EFriendType {
    none("none"), friend("friend"), block("block"), black("black"), unknown("unknown"), all("all");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EFriendType(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EFriendType(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EFriendType> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EFriendType cmd : EFriendType.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EFriendType getType(String cmd) {
        EFriendType ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
