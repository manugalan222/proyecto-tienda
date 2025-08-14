package com.galan.proyectotienda2025;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventarioControlador implements Initializable {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDesc;
    @FXML private TableColumn<Producto, Double> colCompra;
    @FXML private TableColumn<Producto, Double> colVenta;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, String> colTemporada;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Boolean> colPromo;

    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_principal.fxml");
    }

    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_productos.fxml");
    }
    public void onVentasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_venta.fxml");
    }
    public void onClientesButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_clientes.fxml");
    }

    public void onCuotasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_cuotas.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar cómo cada columna obtiene su valor
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
        colVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colTemporada.setCellValueFactory(new PropertyValueFactory<>("temporada"));
        colPromo.setCellValueFactory(new PropertyValueFactory<>("promocionable"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Traer los datos de la BD y mostrarlos
        ObservableList<Producto> productosObservable = FXCollections.observableArrayList(Database.listarProductos());

        tablaProductos.setItems(productosObservable);

    }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }


}
