package com.galan.proyectotienda2025;

import org.h2.tools.Server;

import java.sql.SQLException;

public class H2ServerLauncher {
    private Server server;

    public void start() throws SQLException {
        server = Server.createTcpServer("-tcpAllowOthers").start();
        System.out.println("Servidor H2 iniciado en: " + server.getURL());
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("Servidor H2 detenido.");
        }
    }

    public static void main(String[] args) throws SQLException {
        H2ServerLauncher launcher = new H2ServerLauncher();
        launcher.start();

        // Dejamos corriendo el servidor, para probar conectar desde DBeaver
        System.out.println("Presioná Ctrl+C para detener...");
        while(true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
        }
    }
}
