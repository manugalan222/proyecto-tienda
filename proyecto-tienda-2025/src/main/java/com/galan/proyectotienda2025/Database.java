package com.galan.proyectotienda2025;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("CREE");
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS productos (
                    id VARCHAR(100) PRIMARY KEY,
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
                CREATE TABLE IF NOT EXISTS detalle_venta (
                    id IDENTITY PRIMARY KEY,
                    cliente_id BIGINT NOT NULL,
                    producto_id VARCHAR(100) NOT NULL,
                    monto_total DECIMAL(10,2) NOT NULL,
                    saldo_pendiente DECIMAL(10,2) NOT NULL,
                    es_a_cuotas BOOLEAN NOT NULL,
                    medio_pago VARCHAR(30) NOT NULL,
                    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                    FOREIGN KEY (producto_id) REFERENCES productos(id)
                );
                """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS cuotas (
                        id IDENTITY PRIMARY KEY,
                        detalle_venta_id BIGINT NOT NULL,
                        monto_cuota DECIMAL(10,2) NOT NULL,
                        fecha_vencimiento DATE,
                        fecha_pago TIMESTAMP,
                        estado VARCHAR(20) NOT NULL,
                        FOREIGN KEY (detalle_venta_id) REFERENCES detalle_venta(id)
                    );
                """);

            System.out.println("Base de datos inicializada.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertarProducto(String id, String nombre, double precioCompra, double precioVenta, String temporada,
                                        boolean promocionable, String descripcion, String marca, int stock) {
        String sql = "INSERT INTO productos (id, nombre, precio_compra, precio_venta, temporada_producto, promocionable, descripcion, marca, stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, nombre);
            pstmt.setDouble(3, precioCompra);
            pstmt.setDouble(4, precioVenta);
            pstmt.setString(5, temporada);
            pstmt.setBoolean(6, promocionable);
            pstmt.setString(7, descripcion);
            pstmt.setString(8, marca);
            pstmt.setInt(9, stock);

            pstmt.executeUpdate();
            System.out.println("Producto agregado.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registrarVentaEnCuotas(long cliente_id, String producto_id, int cantidad, double monto_total, int numero_cuotas) {
        Connection conn = null;
        try {
            conn = connect();
            conn.setAutoCommit(false); // Inicia la transacción

            // Paso 1: Insertar en detalle_venta y obtener el ID de la venta
            String sqlVenta = "INSERT INTO detalle_venta (cliente_id, producto_id, cantidad, monto_total, saldo_pendiente, es_a_cuotas) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setLong(1, cliente_id);
            pstmtVenta.setString(2, producto_id);
            pstmtVenta.setInt(3, cantidad);
            pstmtVenta.setDouble(4, monto_total);
            pstmtVenta.setDouble(5, monto_total); // Saldo pendiente es el monto total al inicio
            pstmtVenta.setBoolean(6, true);
            pstmtVenta.executeUpdate();

            ResultSet rs = pstmtVenta.getGeneratedKeys();
            long id_venta = -1;
            if (rs.next()) {
                id_venta = rs.getLong(1);
            }

            // Paso 2: Generar e insertar cada cuota en la tabla 'cuotas'
            double monto_cuota = monto_total / numero_cuotas;
            String sqlCuota = "INSERT INTO cuotas (detalle_venta_id, monto_cuota, estado, fecha_vencimiento) VALUES (?, ?, 'Pendiente', ?)";
            PreparedStatement pstmtCuota = conn.prepareStatement(sqlCuota);
            java.util.Date fecha_vencimiento_base = new java.util.Date();
            for (int i = 0; i < numero_cuotas; i++) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(fecha_vencimiento_base);
                cal.add(java.util.Calendar.MONTH, i + 1); // Vence cada mes
                pstmtCuota.setLong(1, id_venta);
                pstmtCuota.setDouble(2, monto_cuota);
                pstmtCuota.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
                pstmtCuota.addBatch();
            }
            pstmtCuota.executeBatch();

            conn.commit();
            System.out.println("Venta en cuotas registrada.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Leer productos (Select)
    public static List<Producto> listarProductos() {
        String sql = "SELECT * FROM productos";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_compra"),
                        rs.getDouble("precio_venta"),
                        rs.getString("temporada_producto"),
                        rs.getBoolean("promocionable"),
                        rs.getString("descripcion"),
                        rs.getString("marca"),
                        rs.getInt("stock")
                );
                productos.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public static void actualizarProducto(String id, String nombre, double precioCompra, double precioVenta, String temporada,
                                          boolean promocionable, String descripcion, String marca, int stock) {
        String sql = "UPDATE productos SET nombre=?, precio_compra=?, precio_venta=?, temporada_producto=?, promocionable=?, descripcion=?, marca=?, stock=? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precioCompra);
            pstmt.setDouble(3, precioVenta);
            pstmt.setString(4, temporada);
            pstmt.setBoolean(5, promocionable);
            pstmt.setString(6, descripcion);
            pstmt.setString(7, marca);
            pstmt.setInt(8, stock);
            pstmt.setString(9, id);

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Producto actualizado.");
            } else {
                System.out.println("No se encontró producto con id " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Borrar producto (Delete)
    public static void eliminarProducto(String id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Producto eliminado.");
            } else {
                System.out.println("No se encontró producto con id " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Producto obtenerProductoPorId(String id) {
        String sql = "SELECT * FROM productos WHERE id=?";
        Producto producto = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio_compra"),
                            rs.getDouble("precio_venta"),
                            rs.getString("temporada_producto"),
                            rs.getBoolean("promocionable"),
                            rs.getString("descripcion"),
                            rs.getString("marca"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    // Clase auxiliar para mapear resultados
    public static class Producto {
        public String id;
        public String nombre;
        public double precioCompra;
        public double precioVenta;
        public String temporada;
        public boolean promocionable;
        public String descripcion;
        public String marca;
        public int stock;

        public Producto(String id, String nombre, double precioCompra, double precioVenta, String temporada,
                        boolean promocionable, String descripcion, String marca, int stock) {
            this.id = id;
            this.nombre = nombre;
            this.precioCompra = precioCompra;
            this.precioVenta = precioVenta;
            this.temporada = temporada;
            this.promocionable = promocionable;
            this.descripcion = descripcion;
            this.marca = marca;
            this.stock = stock;
        }

        // getters (nombres usados por PropertyValueFactory)
        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public double getPrecioCompra() { return precioCompra; }
        public double getPrecioVenta() { return precioVenta; }
        public String getMarca() { return marca; }
        public String getTemporada() { return temporada; }
        public boolean getPromocionable() { return promocionable; }
        public int getStock() { return stock; }

        public static void insertarDetalleVenta(String producto_id, int cantidad, String nombre, double precioCompra, double precioVenta, String temporada,
                                            boolean promocionable, String descripcion, String marca, int stock) {
            String sql = "INSERT INTO productos (id, nombre, precio_compra, precio_venta, temporada_producto, promocionable, descripcion, marca, stock) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(2, nombre);
                pstmt.setDouble(3, precioCompra);
                pstmt.setDouble(4, precioVenta);
                pstmt.setString(5, temporada);
                pstmt.setBoolean(6, promocionable);
                pstmt.setString(7, descripcion);
                pstmt.setString(8, marca);
                pstmt.setInt(9, stock);

                pstmt.executeUpdate();
                System.out.println("Producto agregado.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return String.format("%s: %s (Compra: $%.2f, Venta: $%.2f) Stock: %d", id, nombre, precioCompra, precioVenta, stock);
        }
    }

}


