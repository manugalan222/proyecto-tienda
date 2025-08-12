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

public class ProductosControlador implements Initializable {

    ProductosModelo productosModelo= new ProductosModelo();
    @FXML TextField txtProductoId = null;
    @FXML TextField txtProductoNombre = null;
    @FXML TextArea txtProductoDesc = null;
    @FXML TextField txtPrecioCompra = null;
    @FXML TextField txtPrecioVenta = null;
    @FXML private ComboBox<String> comboTemporada;
    @FXML private ComboBox<String> comboMarca;
    @FXML private ComboBox<Boolean> comboPromo;
    @FXML private Spinner<Integer> spinnerStock;

    @FXML private TableView<Database.Producto> tablaProductos;
    @FXML private TableColumn<Database.Producto, String> colId;
    @FXML private TableColumn<Database.Producto, String> colNombre;
    @FXML private TableColumn<Database.Producto, String> colDesc;
    @FXML private TableColumn<Database.Producto, Double> colCompra;
    @FXML private TableColumn<Database.Producto, Double> colVenta;
    @FXML private TableColumn<Database.Producto, String> colMarca;
    @FXML private TableColumn<Database.Producto, String> colTemporada;
    @FXML private TableColumn<Database.Producto, Integer> colStock;
    @FXML private TableColumn<Database.Producto, Boolean> colPromo;

    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_principal.fxml");
    }
    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_inventario.fxml");
    }


    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_clientes.fxml");
    }

    public void onVentasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_venta.fxml");
    }

    public void onCuotasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_cuotas.fxml");
    }

    public void onAgregarButtonClick(ActionEvent actionEvent) throws IOException {

        /* Recibe los valores para la base de datos*/
        String temporadaValor = comboTemporada.getValue();
        String marcaValor = comboMarca.getValue();
        Boolean promoValor = comboPromo.getValue();
        int stockValor = spinnerStock.getValue();
        double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
        double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
        String productoId = txtProductoId.getText();
        String productoNombre = txtProductoNombre.getText();
        String productoDesc = txtProductoDesc.getText();

        productosModelo.agregarProductoBd(temporadaValor,marcaValor,stockValor,precioCompra,precioVenta,productoId,productoNombre,productoDesc,promoValor);
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

        // Cargar opciones para Estado
        comboTemporada.getItems().addAll("Disponible", "No disponible", "En pedido");

        // Cargar opciones para Tipo

        // Cargar opciones para Marca
        comboMarca.getItems().addAll("Sin marca");

        // Valor mínimo 0, máximo 1000, valor inicial 0, y paso de 1 en 1
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);

        spinnerStock.setValueFactory(valueFactory);

        //usuario escriba texto inválido
        spinnerStock.setEditable(true);

        comboPromo.getItems().addAll(true,false);

    }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }

}
