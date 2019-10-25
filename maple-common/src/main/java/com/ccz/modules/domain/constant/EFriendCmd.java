package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EFriendCmd {
    addfriend("addfriend"), delfriend("delfriend"),
    changefriendstatus("changefriendstatus"), friendids("friendids"), friendcnt("friendcnt"), friendinfos("friendinfos"),
    appendme("appendme"), blockme("blockme"), appendmecnt("appendmecnt"), blockmecnt("blockmecnt"), none("none");


    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EFriendCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EFriendCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EFriendCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EFriendCmd cmd : EFriendCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EFriendCmd getType(String cmd) {
        EFriendCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
