package com.galan.proyectotienda2025;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductosControlador {
    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException {
        // Cargo el FXML de productos
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_principal.fxml"));

        // Obtengo el Stage actual a partir del evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        double ancho = stage.getWidth();
        double alto = stage.getHeight();
        // Cambio la escena
        stage.setScene(new Scene(root,ancho,alto));
        stage.show();
    }

    public void onInventarioButtonClick() throws IOException {
    }

    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException {
        // Cargo el FXML de productos
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_productos.fxml"));

        // Obtengo el Stage actual a partir del evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        double ancho = stage.getWidth();
        double alto = stage.getHeight();
        // Cambio la escena
        stage.setScene(new Scene(root,ancho,alto));
        stage.show();
    }
}
