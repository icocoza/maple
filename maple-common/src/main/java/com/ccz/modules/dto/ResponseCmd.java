package com.ccz.modules.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ResponseCmd {
    @Setter
    private String cmd;
    @Setter
    private String code;
    @Setter
    private String error;
    private String param;
    private Map<String, Object> mapParam = new HashMap<>();

    public ResponseCmd(String cmd) {
        this.cmd = cmd;
    }

    public ResponseCmd(String cmd, String code) {
        this.cmd = cmd;
        this.code = code;
    }

    public ResponseCmd setParam(String p) {
        this.param = p;
        return this;
    }

    public ResponseCmd setParam(String k, Object obj) {
        mapParam.put(k, obj);
        return this;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = (ObjectNode) objectMapper.convertValue(mapParam, JsonNode.class);
        objectNode.put("cmd", cmd == null ? "unknown" : cmd);
        objectNode.put("code", code == null ? "unknown" : code);
        objectNode.put("result", error);
        if (param != null)
            objectNode.put("param", param);    //user defined values which client want to bypass
        return objectNode.toString();
    }

    public void clear() {
        mapParam.clear();
    }
}