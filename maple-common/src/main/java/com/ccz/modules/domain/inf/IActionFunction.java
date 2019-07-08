package com.ccz.modules.domain.inf;

import com.ccz.modules.dto.ResponseCmd;

import java.io.IOException;

@FunctionalInterface
public interface IActionFunction {
    void doAction(ISessionItem session, ResponseCmd res, String msg) throws IOException;
}