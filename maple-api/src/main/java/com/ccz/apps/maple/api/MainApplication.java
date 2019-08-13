package com.ccz.apps.maple.api;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.server.ServiceServer;
import com.ccz.modules.server.ServiceServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ccz.apps" })
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        MapleServerHandler mapleServerHandlerBean = context.getBean(MapleServerHandler.class);
        try {
            ServiceServer serviceServer = new ServiceServerBuilder().
                    setPort(19112).
                    setWebsocketPath("/wss").
                    setServiceActionHandler(mapleServerHandlerBean/*new MapleServerHandler()*/).build();

            serviceServer.start();
            serviceServer.closeSync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
