
package com.galan.proyectotienda2025;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClienteControlador {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;

    // Metodo para pasar el DNI desde el controlador de ventas.
    public void setDni(String dni) {
        txtDni.setText(dni);
    }

    @FXML
    public void onGuardarClienteClick() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        Database.insertarCliente(nombre, apellido, telefono, dni);
        mostrarAlerta("Cliente registrado con éxito.");

        // Cierra la ventana actual
        Stage stage = (Stage) txtDni.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}