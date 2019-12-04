package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EChannelCmd {
    chcreate("chcreate"), chexit("chexit"), chenter("chenter"), chinvite("chinvite"),
    chmime("chmime"), chcount("chcount"), chlastmsg("chlastmsg"), chinfo("chinfo"), none("none");


    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EChannelCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EChannelCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EChannelCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EChannelCmd cmd : EChannelCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EChannelCmd getType(String cmd) {
        EChannelCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
