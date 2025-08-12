package com.galan.proyectotienda2025;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaControlador {

    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_principal.fxml");
    }

    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_productos.fxml");
    }

    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_inventario.fxml");
    }

    public void onVentaButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_venta.fxml");
    }

    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_clientes.fxml");
    }

    public void onCuotasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_cuotas.fxml");
    }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }
}
