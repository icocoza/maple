package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EUploadFileType {
    png("png"), jpg("jpg"), gif("gif"), none("none");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EUploadFileType(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EUploadFileType(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EUploadFileType> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EUploadFileType cmd : EUploadFileType.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EUploadFileType getType(String cmd) {
        EUploadFileType ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
