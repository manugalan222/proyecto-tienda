package com.galan.proyectotienda2025;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(HelloApplication.class, args);
        System.out.println("Nashe");

        Database.init();

        Application.launch(PantallaAplicacion.class, args);
    }

}
