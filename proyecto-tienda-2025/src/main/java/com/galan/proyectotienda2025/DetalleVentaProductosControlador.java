package com.galan.proyectotienda2025;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class DetalleVentaProductosControlador {

    @FXML private Label lblVentaId;
    @FXML private TableView<LineaDeVenta> tablaProductos;
    @FXML private TableColumn<LineaDeVenta, String> colNombreProducto;
    @FXML private TableColumn<LineaDeVenta, Integer> colCantidad;
    @FXML private TableColumn<LineaDeVenta, Double> colSubtotal;

    private ObservableList<LineaDeVenta> productosData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tablaProductos.setItems(productosData);
    }
    
    public void setVentaId(long ventaId) {
        lblVentaId.setText("ID de Venta: " + ventaId);
        List<LineaDeVenta> productos = Database.obtenerProductosPorVenta(ventaId);
        productosData.addAll(productos);
    }
}