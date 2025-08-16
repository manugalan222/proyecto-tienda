package com.galan.proyectotienda2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaAplicacion extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database.init();
        //Database.modificarColumnaPromo();
        //Database.insertarProducto("1","Remera", 100, 150, "verano", true, "Remera de algodón", "MarcaX", 50);
        //Database.insertarProducto("2","Tanga", 100, 150, "verano", true, "Tanga Comoda", "MarcaX", 50);
        //Database.actualizarProducto(3,"Remera",100,300,"Verano",false, "Remera de algodon", "Nike", 10);
        //Database.eliminarProducto(3);
        //Database.listarProductos();

        var productos = Database.listarProductos();
        productos.forEach(System.out::println);

        FXMLLoader fxmlLoader = new FXMLLoader(PantallaAplicacion.class.getResource("pantalla_principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(PantallaControlador.class.getResource("estilos.css").toExternalForm());
        stage.setTitle("MVC!");
        stage.setScene(scene);
        stage.show();
    }

}
