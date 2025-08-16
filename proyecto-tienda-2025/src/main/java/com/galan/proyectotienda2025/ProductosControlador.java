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
import javafx.scene.chart.PieChart;
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
    @FXML private TextField txtMarca;
    @FXML private ComboBox<String> comboPromo;
    @FXML private Spinner<Integer> spinnerStock;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDesc;
    @FXML private TableColumn<Producto, Double> colCompra;
    @FXML private TableColumn<Producto, Double> colVenta;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, String> colTemporada;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colPromo;

    private ObservableList<Producto> productosObservable = FXCollections.observableArrayList(Database.listarProductos());

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

    public void onResetButtonClick(ActionEvent actionEvent) {
        txtProductoId.clear();
        txtProductoNombre.clear();
        txtProductoDesc.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        comboTemporada.getSelectionModel().clearSelection();
        txtMarca.clear();
        comboPromo.getSelectionModel().clearSelection();
        spinnerStock.getValueFactory().setValue(0);
    }


    public void onAgregarButtonClick(ActionEvent actionEvent) throws IOException {

        /* Recibe los valores para la base de datos*/
        String temporadaValor = comboTemporada.getValue();
        String marcaValor = txtMarca.getText();
        String promoValor = comboPromo.getValue();
        int stockValor = spinnerStock.getValue();
        double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
        double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
        String productoId = txtProductoId.getText();
        String productoNombre = txtProductoNombre.getText();
        String productoDesc = txtProductoDesc.getText();

        productosModelo.agregarProductoBd(temporadaValor,marcaValor,stockValor,precioCompra,precioVenta,productoId,productoNombre,productoDesc,promoValor);

        actualizarTabla();

    }

    public void onBorrarButtonClick(ActionEvent actionEvent) throws IOException {
        Database.eliminarProducto(txtProductoId.getText());
        actualizarTabla();
    }

    public void onActualizarButtonClick(ActionEvent actionEvent) throws IOException {

        Database.actualizarProducto(txtProductoId.getText(), txtProductoNombre.getText(),Double.parseDouble(txtPrecioCompra.getText()),
                Double.parseDouble(txtPrecioVenta.getText()),comboTemporada.getValue(), comboPromo.getValue(),
                txtProductoDesc.getText(),txtMarca.getText(),spinnerStock.getValue());

        actualizarTabla();
    }

    //Esta es para actualizar el contenido de las tablas cuando se borran tablas o actualizan ya tu sabe
    public void actualizarTabla(){
        productosObservable.clear();
        productosObservable.addAll(Database.listarProductos());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboTemporada.getItems().addAll("Disponible", "No disponible", "En pedido");

        //comboMarca.getItems().addAll("Sin marca");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1);

        spinnerStock.setValueFactory(valueFactory);

        spinnerStock.setEditable(true);

        comboPromo.getItems().addAll("Si","No");

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
        tablaProductos.setItems(productosObservable);

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarProductoEnCampos(newSelection);
            }
        });

    }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }

    private void cargarProductoEnCampos(Producto producto) {
        txtProductoId.setText(producto.getId());
        txtProductoNombre.setText(producto.getNombre());
        txtProductoDesc.setText(producto.getDescripcion());
        txtPrecioCompra.setText(String.valueOf(producto.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
        comboTemporada.setValue(producto.getTemporada());
        //comboMarca.setValue(producto.getMarca());
        txtMarca.setText(producto.getMarca());
        comboPromo.setValue(producto.getPromocionable());
        spinnerStock.getValueFactory().setValue(producto.getStock());
    }


}
