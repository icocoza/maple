package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EFileCmd {
    uploadFile("uploadFile"), multiUploadFile("multiUploadFile"), none("none");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EFileCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EFileCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EFileCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EFileCmd cmd : EFileCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EFileCmd getType(String cmd) {
        EFileCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
