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

    @FXML private TextField txtDniCliente;
    @FXML private CheckBox chkCuotas;
    @FXML ComboBox<String> comboTipoCuotas;
    @FXML private TextField txtNumCuotas;
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

        comboTipoCuotas.setItems(FXCollections.observableArrayList("Diaria", "Semanal", "Mensual"));
        comboTipoCuotas.setDisable(true);

        chkCuotas.selectedProperty().addListener((observable, oldValue, newValue) -> {
            comboTipoCuotas.setDisable(!newValue);
            if (newValue) {
                comboTipoCuotas.getSelectionModel().selectFirst();
            } else {
                txtNumCuotas.setText("");
                comboTipoCuotas.getSelectionModel().clearSelection();
            }
        });

        // Listener para el ComboBox 'Tipo de Cuota'
        comboTipoCuotas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "Diaria":
                        txtNumCuotas.setText("200");
                        break;
                    case "Semanal":
                        txtNumCuotas.setText("28"); // 200 días / 7 días
                        break;
                    case "Mensual":
                        txtNumCuotas.setText("3"); // De acuerdo a tu regla
                        break;
                    default:
                        txtNumCuotas.setText("");
                        break;
                }
            }
        });
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
        if (carrito.isEmpty()) {
            mostrarAlerta("No hay productos en la venta.");
            return;
        }

        String dni = txtDniCliente.getText().trim();
        if (dni.isEmpty()) {
            mostrarAlerta("Por favor, ingrese el DNI del cliente.");
            return;
        }

        Database.Cliente cliente = Database.obtenerClientePorDni(dni);

        if (cliente == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cliente No Encontrado");
            alert.setHeaderText("El cliente con DNI " + dni + " no está registrado.");
            alert.setContentText("¿Desea registrar un nuevo cliente ahora?");

            ButtonType botonSi = new ButtonType("Sí");
            ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(botonSi, botonNo);

            alert.showAndWait().ifPresent(result -> {
                if (result == botonSi) {
                    mostrarFormularioRegistroCliente(dni);
                }
            });
            return;
        }

        long clienteId = cliente.id;
        String medioPago = comboMetodoPago.getValue();
        boolean esEnCuotas = chkCuotas.isSelected();
        String tipoCuotas = comboTipoCuotas.getValue();

        int numCuotas = 0;
        if (esEnCuotas) {
            try {
                numCuotas = Integer.parseInt(txtNumCuotas.getText().trim());
                if (numCuotas <= 0) {
                    mostrarAlerta("Número de cuotas inválido.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Número de cuotas inválido.");
                return;
            }
        }

        // Llamada al metodo unificado de la base de datos
        Database.insertarVenta(clienteId, carrito, medioPago, esEnCuotas, numCuotas, tipoCuotas);
        mostrarAlerta("Venta registrada con éxito.");

        // Limpiar el formulario después de la venta
        carrito.clear();
        calcularPrecioFinal();
        txtDniCliente.clear();
        txtNumCuotas.clear();
    }

    private void mostrarFormularioRegistroCliente(String dni) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pantalla_cliente.fxml"));
            Parent root = fxmlLoader.load();

            // Pasamos el DNI al controlador del nuevo formulario
            ClienteControlador clienteControlador = fxmlLoader.getController();
            clienteControlador.setDni(dni);

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // showAndWait para esperar el registro antes de continuar

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el formulario de registro de cliente.");
        }
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
