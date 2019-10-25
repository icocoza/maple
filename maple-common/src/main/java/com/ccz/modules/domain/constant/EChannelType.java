package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EChannelType {
    oneToOne("oneToOne"), group("group"), openChannel("openChannel"), none("none");


    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EChannelType(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EChannelType(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EChannelType> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EChannelType cmd : EChannelType.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EChannelType getType(String cmd) {
        EChannelType ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
