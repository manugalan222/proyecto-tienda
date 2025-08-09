package com.galan.proyectotienda2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaAplicacion extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PantallaAplicacion.class.getResource("pantalla_principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //scene.getStylesheets().add(PantallaControlador.class.getResource("estilos.css").toExternalForm());
        stage.setTitle("MVC!");
        stage.setScene(scene);
        stage.show();
    }
}
