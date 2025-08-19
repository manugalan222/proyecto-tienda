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
import javafx.stage.Stage;
import org.h2.store.Data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CuotasControlador {

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
        Database.RegistrarPagoCuota(1);
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
    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException { cambiarEscena(actionEvent, "pantalla_clientes.fxml"); }
    public void onVentaButtonClick(ActionEvent actionEvent) throws IOException{ cambiarEscena(actionEvent, "pantalla_venta.fmxl");}


    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }


    public void onCuotasButtonClick(ActionEvent actionEvent) {

    }
}
