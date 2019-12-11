package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EAdminCmd {
    none("none"), 	adminRegister("adminRegister"), adminLogin("adminLogin"), adminLogout("adminLogout"),
    adminAddApp("adminAddApp"), adminDelApp("adminDelApp"), adminAppList("adminAppList"), adminModifyApp("adminModifyApp"),
    adminAppCount("adminAppCount"), adminStopApp("adminStopApp"), adminRunApp("adminRunApp"), adminReadyApp("adminReadyApp");


    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EAdminCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EAdminCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public static final Map<String, EAdminCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EAdminCmd cmd : EAdminCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EAdminCmd getType(String cmd) {
        EAdminCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
