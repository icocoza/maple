package com.ccz.apps.maple.api;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.server.ServiceServer;
import com.ccz.modules.server.ServiceServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        try {
            ServiceServer serviceServer = new ServiceServerBuilder().
                    setPort(19112).
                    setWebsocketPath("/wss").
                    setServiceActionHandler(new MapleServerHandler()).build();

            serviceServer.start();
            serviceServer.closeSync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
