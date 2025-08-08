package com.galan.proyectotienda2025;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:h2:./data/tienda"; // Persistente en carpeta /data
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Abre una conexión a la base de datos
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Inicializa la base y crea la tabla si no existe
    public static void init() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS productos (
                    id IDENTITY PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    precio_compra DECIMAL(10,2) NOT NULL,
                    precio_venta DECIMAL(10,2) NOT NULL,
                    temporada_producto VARCHAR(30) NOT NULL,
                    promocionable BOOLEAN NOT NULL,
                    descripcion TEXT,
                    marca VARCHAR(50) NOT NULL,
                    stock INT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    id IDENTITY PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    telefono VARCHAR(20) NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS detalle_compra (
                    id IDENTITY PRIMARY KEY,
                    cliente_id BIGINT NOT NULL,
                    producto_id BIGINT NOT NULL,
                    cantidad INT NOT NULL,
                    monto_total DECIMAL(10,2) NOT NULL,
                    medio_pago VARCHAR(30) NOT NULL,
                    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                    FOREIGN KEY (producto_id) REFERENCES productos(id)
                );
                """);

            System.out.println("Base de datos inicializada.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertarProducto(String nombre, double precio, int stock) {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();

            System.out.println("Producto agregado.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarProductos() {
        String sql = "SELECT * FROM productos";
        try (Connection conn = connect();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("%d - %s - $%.2f - Stock: %d%n",
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
