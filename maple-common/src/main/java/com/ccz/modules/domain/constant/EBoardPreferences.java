package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EBoardPreferences {
    none("none"), like("like"), dislike("dislike"), happy("happy"), smile("smile"), sad("sad");

    @Getter
    private final String value;
    @Getter
    private final boolean needSession;

    private EBoardPreferences(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EBoardPreferences(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EBoardPreferences> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EBoardPreferences cmd : EBoardPreferences.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EBoardPreferences getType(String cmd) {
        EBoardPreferences ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
