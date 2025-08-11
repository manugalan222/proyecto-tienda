module com.galan.proyectotienda2025 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires com.h2database;

    opens com.galan.proyectotienda2025 to javafx.fxml;
    exports com.galan.proyectotienda2025;
    exports com.galan.proyectotienda2025.controlador;
    opens com.galan.proyectotienda2025.controlador to javafx.fxml;
    exports com.galan.proyectotienda2025.modelo;
    opens com.galan.proyectotienda2025.modelo to javafx.fxml;
}