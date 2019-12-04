package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EBoardContentType {
    text("text"), json("json"), image("image"), vote("vote"),
    audio("audio"), video("video"), link("link"), none("none");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EBoardContentType(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EBoardContentType(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EBoardContentType> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EBoardContentType cmd : EBoardContentType.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EBoardContentType getType(String cmd) {
        EBoardContentType ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
