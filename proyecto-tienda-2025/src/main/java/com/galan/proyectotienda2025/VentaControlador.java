package com.galan.proyectotienda2025;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class VentaControlador {

    @FXML private TextField txtProductoId;
    @FXML private TableView<VentaItem> tablaProductos;
    @FXML private TableColumn<VentaItem, String> columnaId;
    @FXML private TableColumn<VentaItem, String> columnaNombre;
    @FXML private TableColumn<VentaItem, Double> columnaPrecio;
    @FXML private TableColumn<VentaItem, Integer> columnaCantidad;
    @FXML private TableColumn<VentaItem, Double> columnaSubtotal;
    @FXML private ComboBox<String> comboMetodoPago;
    @FXML private TextField txtDescuento;
    @FXML private TextField txtPrecioFinal;

    private ObservableList<VentaItem> carrito = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnaId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        columnaNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        columnaPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrecio()));
        columnaCantidad.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCantidad()));
        columnaSubtotal.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSubtotal()));

        tablaProductos.setItems(carrito);

        comboMetodoPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));
        comboMetodoPago.getSelectionModel().selectFirst();
    }

    @FXML
    public void onAñadirProductoClick() {
        String id = txtProductoId.getText().trim();
        if (id.isEmpty()) return;

        Database.Producto producto = Database.obtenerProductoPorId(id);
        if (producto == null) {
            mostrarAlerta("Producto no encontrado");
            return;
        }

        // Si ya existe en el carrito, aumentar cantidad
        for (VentaItem item : carrito) {
            if (item.getId().equals(producto.id)) {
                item.setCantidad(item.getCantidad() + 1);
                tablaProductos.refresh();
                calcularPrecioFinal();
                txtProductoId.clear();
                return;
            }
        }

        carrito.add(new VentaItem(producto, 1));
        calcularPrecioFinal();
        txtProductoId.clear();

    }

    @FXML
    public void onFinalizarVentaClick() {
    }



    private double calcularPrecioFinal() {
        double total = carrito.stream().mapToDouble(VentaItem::getSubtotal).sum();
        double descuento = 0;
        try {
            if (!txtDescuento.getText().isEmpty()) {
                descuento = Double.parseDouble(txtDescuento.getText());
            }
        } catch (NumberFormatException ignored) {}

        total -= total * (descuento / 100);
        txtPrecioFinal.setText(String.format("%.2f", total));
        return total;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos de navegación que ya tenías
    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_productos.fxml"); }
    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_principal.fxml"); }
    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_inventario.fxml"); }
    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_clientes.fxml"); }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }
}
