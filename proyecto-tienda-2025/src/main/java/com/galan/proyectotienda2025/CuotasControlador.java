package com.galan.proyectotienda2025;

import com.galan.proyectotienda2025.Database;
import com.galan.proyectotienda2025.Cuotas;
import com.galan.proyectotienda2025.Cliente;
//import com.galan.proyectotienda2025.Venta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.h2.store.Data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CuotasControlador {

    @FXML private TextField txtCuotasAPagar;
    @FXML private TextField txtBuscarCuota;
    @FXML private TableView<ResumenVenta> tablaCuotasPendientes;
    //@FXML private TableColumn<ResumenVenta, Long> colCuotaId;
    @FXML private TableColumn<ResumenVenta, Long> colVentaId;
    @FXML private TableColumn<ResumenVenta, String> colClienteDni;
    @FXML private TableColumn<ResumenVenta, Long> colMontoTotal;
    @FXML private TableColumn<ResumenVenta, String> colTipoCuota;
    @FXML private TableColumn<ResumenVenta, Long> colSaldoRestante;
    @FXML private TableColumn<ResumenVenta, Integer> colTotalCuotas;
    @FXML private TableColumn<ResumenVenta, Integer> colCuotasPagadas;
    @FXML private TableColumn<ResumenVenta, Integer> colCuotasPendientes;
    @FXML private TableColumn<ResumenVenta, LocalDate> colFechaLimite;
    @FXML private Button btnRegistrarPago;
    @FXML private Button btnRecordarPago;

    private ObservableList<ResumenVenta> cuotasData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inicializar las columnas de la tabla
        //colCuotaId.setCellValueFactory(new PropertyValueFactory<>("cuotaId"));
        colVentaId.setCellValueFactory(new PropertyValueFactory<>("ventaId"));
        colClienteDni.setCellValueFactory(new PropertyValueFactory<>("clienteDni"));
        colMontoTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        colTipoCuota.setCellValueFactory(new PropertyValueFactory<>("tipoCuota"));
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fechaPlazo200Dias"));
        colSaldoRestante.setCellValueFactory(new PropertyValueFactory<>("saldoRestante"));
        colTotalCuotas.setCellValueFactory(new PropertyValueFactory<>("cuotasTotales"));
        colCuotasPagadas.setCellValueFactory(new PropertyValueFactory<>("cuotasPagadas"));
        colCuotasPendientes.setCellValueFactory(new PropertyValueFactory<>("cuotasPendientes"));
        //colTipoCuota.setCellValueFactory(new PropertyValueFactory<>("tipoCuota"));

        // Enlazar la lista observable a la tabla
        tablaCuotasPendientes.setItems(cuotasData);

        // Cargar los datos iniciales
        cargarCuotasPendientes();

        // Deshabilitar los botones hasta que se seleccione una fila
        btnRegistrarPago.setDisable(true);
        tablaCuotasPendientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnRegistrarPago.setDisable(newSelection == null);
        });
    }

    private void cargarCuotasPendientes() {
        // Limpiar la lista existente
        cuotasData.clear();
        // Obtener la lista de cuotas pendientes de la base de datos
        List<ResumenVenta> resumenVentas = Database.obtenerResumenVentas();
        // Agregar los elementos a la lista observable
        cuotasData.addAll(resumenVentas);
    }

    @FXML
    public void onRegistrarPagoClick() {
        ResumenVenta ventaSeleccionada = tablaCuotasPendientes.getSelectionModel().getSelectedItem();

        // Verificación de selección de venta.
        if (ventaSeleccionada == null) {
            mostrarAlerta("Error"+"Por favor, seleccione una venta de la tabla.");
            return;
        }

        // Verificación de la cantidad de cuotas.
        String cuotasStr = txtCuotasAPagar.getText();
        if (cuotasStr == null || cuotasStr.trim().isEmpty()) {
            mostrarAlerta("Error" + "Por favor, ingrese la cantidad de cuotas a pagar.");
            return;
        }

        int numeroCuotas;
        try {
            numeroCuotas = Integer.parseInt(cuotasStr);
            if (numeroCuotas <= 0) {
                mostrarAlerta("Error"+ "La cantidad de cuotas debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error" + "Por favor, ingrese un número válido para las cuotas.");
            return;
        }

        // Llamamos a la nueva función de la base de datos para registrar el pago.
        Database.registrarPagoCuotas(ventaSeleccionada.getVentaId(), numeroCuotas);

        // Recargamos la tabla para que se muestren los cambios.
        cargarCuotasPendientes();

        // Limpiamos el campo de texto.
        txtCuotasAPagar.clear();
        mostrarAlerta("Éxito" + "Pago de cuotas registrado con éxito.");
    }

    @FXML
    public void onBuscarCuotaClick() {
        cuotasData.clear();
        String dni = txtBuscarCuota.getText().trim();
        if (!dni.isEmpty()) {
            cuotasData.addAll(Database.buscarResumenVentasPorDni(dni));
        } else {
            cargarCuotasPendientes();
        }
    }

    @FXML
    public void onTablaDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
            ResumenVenta ventaSeleccionada = tablaCuotasPendientes.getSelectionModel().getSelectedItem();
            if (ventaSeleccionada != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_productos_comprados.fxml"));
                    Parent root = loader.load();

                    DetalleVentaProductosControlador controller = loader.getController();
                    controller.setVentaId(ventaSeleccionada.getVentaId());

                    Stage stage = new Stage();
                    stage.setTitle("Productos de la Venta " + ventaSeleccionada.getVentaId());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    mostrarAlerta("Error" + "No se pudo cargar la vista de detalles de productos.");
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void onLimpiarBusquedaClick() {
        txtBuscarCuota.clear();
        cargarCuotasPendientes();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_productos.fxml"); }
    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_principal.fxml"); }
    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_inventario.fxml"); }
    public void onClientesButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_clientes.fxml"); }
    public void onVentasButtonClick(ActionEvent actionEvent) throws IOException{ cambiarEscena(actionEvent, "pantalla_venta.fmxl");}


    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }

}
