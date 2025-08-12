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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.galan.proyectotienda2025.Database;
public class InventarioControlador implements Initializable {

    @FXML
    private TableView<Database.Producto> tablaProductos;
    @FXML private TableColumn<Database.Producto, String> colId;
    @FXML private TableColumn<Database.Producto, String> colNombre;
    @FXML private TableColumn<Database.Producto, String> colDesc;
    @FXML private TableColumn<Database.Producto, Double> colCompra;
    @FXML private TableColumn<Database.Producto, Double> colVenta;
    @FXML private TableColumn<Database.Producto, String> colMarca;
    @FXML private TableColumn<Database.Producto, String> colTemporada;
    @FXML private TableColumn<Database.Producto, String> colTipo;
    @FXML private TableColumn<Database.Producto, Integer> colStock;
    @FXML private TableColumn<Database.Producto, Boolean> colPromo;

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
    public void onVentasButtonClick(ActionEvent actionEvent) throws IOException {

        // Cargo el FXML de productos
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_venta.fxml"));

        // Obtengo el Stage actual a partir del evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        double ancho = stage.getWidth();
        double alto = stage.getHeight();

        // Cambio la escena
        stage.setScene(new Scene(root,ancho,alto));
        stage.show();
    }
    public void onClientesButtonClick(ActionEvent actionEvent) throws IOException {

        // Cargo el FXML de productos
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_clientes.fxml"));

        // Obtengo el Stage actual a partir del evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        double ancho = stage.getWidth();
        double alto = stage.getHeight();

        // Cambio la escena
        stage.setScene(new Scene(root,ancho,alto));
        stage.show();
    }

    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException {

        // Cargo el FXML de productos
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_inventario.fxml"));

        // Obtengo el Stage actual a partir del evento
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        double ancho = stage.getWidth();
        double alto = stage.getHeight();

        // Cambio la escena
        stage.setScene(new Scene(root,ancho,alto));
        stage.show();
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
        ObservableList<Database.Producto> productosObservable = FXCollections.observableArrayList(Database.listarProductos());

        tablaProductos.setItems(productosObservable);

    }
}
