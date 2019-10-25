package com.ccz.modules.domain.constant;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EBoardCmd {
    getcategorylist("getcategorylist"), addboard("addboard"), delboard("delboard"),
    updatetitle("updatetitle"), updatecontent("updatecontent"), updatecategory("updatecategory"), updateboard("updateboard"), boardlist("boardlist"), getcontent("getcontent"),
    like("like"), dislike("dislike"),
    addreply("addreply"), delreply("delreply"), replylist("replylist"),
    addvote("addvote"), selvote("selvote"), voteitemlist("voteitemlist"), voteupdate("voteupdate"), changeselection("changeselection"),
    voteinfolist("voteinfolist"), boardsearch("boardsearch"), none("none");

    @Getter
    private final String value;
    @Getter private final boolean needSession;

    private EBoardCmd(String value) {
        this.value = value;
        this.needSession = true;
    }

    private EBoardCmd(String value, boolean needSession) {
        this.value = value;
        this.needSession = needSession;
    }

    public String getValue() {
        return value;
    }

    public static final Map<String, EBoardCmd> StrToAptCmdMap;

    static {
        StrToAptCmdMap = new ConcurrentHashMap<>();
        for(EBoardCmd cmd : EBoardCmd.values())
            StrToAptCmdMap.put(cmd.getValue(), cmd);
    }

    static public EBoardCmd getType(String cmd) {
        EBoardCmd ecmd = StrToAptCmdMap.get(cmd);
        if(ecmd != null)
            return ecmd;
        return none;
    }
}
