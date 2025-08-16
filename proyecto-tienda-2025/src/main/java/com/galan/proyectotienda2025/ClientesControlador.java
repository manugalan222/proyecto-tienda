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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientesControlador implements Initializable {

    @FXML private TableView<Cliente> tablaClientes;

    @FXML private TableColumn<Cliente, Long> colClienteId;
    @FXML private TableColumn<Cliente, String> colClienteNombre;
    @FXML private TableColumn<Cliente, String> colClienteApellido;
    @FXML private TableColumn<Cliente, String> colClienteDni;
    @FXML private TableColumn<Cliente, String> colClienteTelefono;

    private boolean esEdicion = false;

    private ObservableList<Cliente> clienteObsarvable = FXCollections.observableArrayList(Database.listarCliente());

    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent,"pantalla_inventario.fxml");
    }
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

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClienteNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colClienteApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colClienteDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colClienteTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaClientes.setItems(clienteObsarvable);
    }


    private void mostrarFormularioRegistroCliente() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pantalla_cliente.fxml"));
            Parent root = fxmlLoader.load();

            // Pasamos el DNI al controlador del nuevo formulario
            PopUpClienteControlador popUpClienteControlador = fxmlLoader.getController();
            //popUpClienteControlador.setDni(dni);

            if(esEdicion){
                popUpClienteControlador.setClienteParaEdicion(tablaClientes.getSelectionModel().getSelectedItem());
            }else{
                popUpClienteControlador.setEsEdicion(esEdicion);
            }

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // showAndWait para esperar el registro antes de continuar

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el formulario de registro de cliente.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void onNuevoClienteButtonClick(ActionEvent actionEvent) {
        esEdicion = false;
        mostrarFormularioRegistroCliente();
    }


    public void onEditarClienteButtonClick(ActionEvent actionEvent) {
        esEdicion = true;
        mostrarFormularioRegistroCliente();
    }

    /*Esto creo que lo deberia mover a popUpclientecontrolador... lo estoy pensando*/
    public void onEliminarClienteButtonClick(ActionEvent actionEvent) {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado != null) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmar borrado");
                alerta.setHeaderText("¿Seguro que querés borrar este cliente?");
                alerta.setContentText("Cliente: " + clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido());

                // Mostrar y esperar la respuesta
                Optional<ButtonType> resultado = alerta.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    // Acá llamás a tu método que borra de la DB
                    Database.eliminarCliente(clienteSeleccionado.getId());

                    // Y refrescás la tabla
                    tablaClientes.getItems().remove(clienteSeleccionado);

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Cliente borrado");
                    info.setHeaderText(null);
                    info.setContentText("El cliente fue eliminado con éxito.");
                    info.showAndWait();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Atención");
                alerta.setHeaderText(null);
                alerta.setContentText("No seleccionaste ningún cliente.");
                alerta.showAndWait();
            }
        }

}
