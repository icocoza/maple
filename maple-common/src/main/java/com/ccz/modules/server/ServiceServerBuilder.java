package com.ccz.modules.server;

import com.ccz.modules.server.handler.IServiceActionHandler;

public class ServiceServerBuilder {
    private int port;
    private String websocketPath;
    private IServiceActionHandler serviceActionHandler;

    public ServiceServerBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ServiceServerBuilder setWebsocketPath(String websocketPath) {
        this.websocketPath = websocketPath;
        return this;
    }

    public ServiceServerBuilder setServiceActionHandler(IServiceActionHandler serviceActionHandler) {
        this.serviceActionHandler = serviceActionHandler;
        return this;
    }

    public ServiceServer build() {
        return new ServiceServer(port, websocketPath, serviceActionHandler);
    }

}
