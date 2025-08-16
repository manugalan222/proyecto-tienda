
package com.galan.proyectotienda2025;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopUpClienteControlador {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;

    // Metodo para pasar el DNI desde el controlador de ventas.
    public void setDni(String dni) {
        txtDni.setText(dni);
    }
    private boolean esEdicion = false;
    private Cliente clienteActual;

    public void setClienteParaEdicion(Cliente cliente) {
        this.clienteActual = cliente;
        this.esEdicion = true;

        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDni.setText(cliente.getDni());
        txtTelefono.setText(cliente.getTelefono());
    }

    @FXML
    public void onGuardarClienteClick() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        Long id = clienteActual.getId();
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        if(!esEdicion){
            Database.insertarCliente(nombre, apellido, telefono, dni);
        }else{
            Database.actualizarCliente(id,nombre,apellido,dni,telefono);
        }

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

    /*esto es re contra mejorable.. pero bueno, por el momento */
    public void setEsEdicion(boolean esEdicion){
        this.esEdicion = esEdicion;
    }


}