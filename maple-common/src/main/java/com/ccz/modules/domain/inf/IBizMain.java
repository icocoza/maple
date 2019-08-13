package com.ccz.modules.domain.inf;

import java.util.Map;

public interface IBizMain {

    void init();
    Map<String, ICommandFunction> getCommandFunctions();

}
