package com.galan.proyectotienda2025;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductosControlador implements Initializable {

    ProductosModelo productosModelo= new ProductosModelo();

    @FXML
    private ComboBox<String> comboEstado;
    @FXML
    private ComboBox<String> comboTipo;
    @FXML
    private ComboBox<String> comboMarca;
    @FXML
    private Spinner<Integer> spinnerStock;


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


    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException {
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

    public void onAgregarButtonClick(ActionEvent actionEvent) throws IOException {
        TextField txtProductoId = null;
        TextField txtProductoNombre = null;
        TextField txtProductoDesc = null;

            /*
        precio_compra DECIMAL(10,2) NOT NULL,
        precio_venta DECIMAL(10,2) NOT NULL,
        temporada_producto VARCHAR(30) NOT NULL,
        promocionable BOOLEAN NOT NULL,
        marca VARCHAR(50) NOT NULL,
        stock INT NOT NULL */
        String productoId = txtProductoId.getText();
        String productoNombre = txtProductoNombre.getText();
        String productoDesc = txtProductoDesc.getText();
        productosModelo.agregarProductoBd(productoId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar opciones para Estado
        comboEstado.getItems().addAll("Disponible", "No disponible", "En pedido");

        // Cargar opciones para Tipo
        comboTipo.getItems().addAll("Pantalon", "Remera", "Campera", "Beanies");

        // Cargar opciones para Marca
        comboMarca.getItems().addAll("Sin marca");

        // Valor mínimo 0, máximo 1000, valor inicial 0, y paso de 1 en 1
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);

        spinnerStock.setValueFactory(valueFactory);

        // Opcional: evitar que el usuario escriba texto inválido
        spinnerStock.setEditable(true);

    }
}
