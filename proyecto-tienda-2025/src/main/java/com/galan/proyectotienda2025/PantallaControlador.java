package com.galan.proyectotienda2025;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;

public class PantallaControlador {

    @FXML
    private Text textoBienvenida;
    @FXML BarChart<String, Number> graficoRendimiento;

    public void initialize() {
        String saludo = getSaludo("Laure");
        textoBienvenida.setText(saludo);
        
        // el grafico de barras

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas");

        series.getData().add(new XYChart.Data<>("Lunes", 50));
        series.getData().add(new XYChart.Data<>("Martes", 80));
        series.getData().add(new XYChart.Data<>("Miércoles", 60));
        series.getData().add(new XYChart.Data<>("Jueves", 90));
        series.getData().add(new XYChart.Data<>("Viernes", 120));
        series.getData().add(new XYChart.Data<>("Sábado", 70));
        series.getData().add(new XYChart.Data<>("Domingo", 40));

        graficoRendimiento.getData().add(series);
    }

    private String getSaludo(String nombre) {
        LocalTime hora = LocalTime.now();
        int h = hora.getHour();

        if (h >= 6 && h < 12) {
            return "¡Buen día, " + nombre + "! ☀️";
        } else if (h >= 12 && h < 20) {
            return "¡Buenas tardes, " + nombre + "! 🌤️";
        } else {
            return "¡Buenas noches, " + nombre + "! 🌙";
        }
    }



    public void onInicioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_principal.fxml");
    }

    public void onProductosButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_productos.fxml");
    }

    public void onInventarioButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_inventario.fxml");
    }

    public void onVentaButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_venta.fxml");
    }

    public void onClienteButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_clientes.fxml");
    }

    public void onCuotasButtonClick(ActionEvent actionEvent) throws IOException {
        cambiarEscena(actionEvent, "pantalla_cuotas.fxml");
    }

    private void cambiarEscena(ActionEvent actionEvent, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }
}
