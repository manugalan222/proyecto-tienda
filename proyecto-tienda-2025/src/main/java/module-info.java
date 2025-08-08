module com.galan.proyectotienda2025 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens com.galan.proyectotienda2025 to javafx.fxml;
    exports com.galan.proyectotienda2025;
}