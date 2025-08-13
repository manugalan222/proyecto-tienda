package com.galan.proyectotienda2025;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.time.LocalDate;
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
                    telefono VARCHAR(20) NOT NULL,
                    dni VARCHAR(20) UNIQUE NOT NULL
                );
           """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS venta (
                id IDENTITY PRIMARY KEY,
                cliente_id BIGINT NOT NULL,
                monto_total DECIMAL(10,2) NOT NULL,
                saldo_pendiente DECIMAL(10,2) NOT NULL,
                es_a_cuotas BOOLEAN NOT NULL,
                medio_pago VARCHAR(30) NOT NULL,
                fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (cliente_id) REFERENCES cliente(id)
                );
            """);


            stmt.execute("""
            CREATE TABLE IF NOT EXISTS linea_de_venta (
                venta_id BIGINT NOT NULL,
                producto_id VARCHAR(100) NOT NULL,
                cantidad INT NOT NULL,
                subtotal DECIMAL(10,2) NOT NULL,
                PRIMARY KEY (venta_id, producto_id),
                FOREIGN KEY (venta_id) REFERENCES venta(id),
                FOREIGN KEY (producto_id) REFERENCES productos(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cuotas (
                    id IDENTITY PRIMARY KEY,
                    venta_id BIGINT NOT NULL,
                    monto_cuota DECIMAL(10,2) NOT NULL,
                    fecha_vencimiento DATE NOT NULL,
                    fecha_pago TIMESTAMP,
                    estado VARCHAR(20) NOT NULL,
                    tipo_cuota VARCHAR(20) NOT NULL,
                    FOREIGN KEY (venta_id) REFERENCES venta(id)
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

    public static void insertarVenta(long clienteId, ObservableList<VentaItem> carrito, String medioPago, boolean esEnCuotas, int numCuotas, String tipoCuota) {
        Connection conn = null;
        try {
            conn = connect();
            conn.setAutoCommit(false); // Inicia la transacción

            // 1. Insertar el registro principal de la venta
            String sqlVenta = "INSERT INTO venta (cliente_id, monto_total, saldo_pendiente, es_a_cuotas, medio_pago) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);

            double montoTotalVenta = carrito.stream().mapToDouble(VentaItem::getSubtotal).sum();
            double saldoPendiente = esEnCuotas ? montoTotalVenta : 0.0;

            pstmtVenta.setLong(1, clienteId);
            pstmtVenta.setDouble(2, montoTotalVenta);
            pstmtVenta.setDouble(3, saldoPendiente);
            pstmtVenta.setBoolean(4, esEnCuotas);
            pstmtVenta.setString(5, medioPago);
            pstmtVenta.executeUpdate();

            ResultSet rsVenta = pstmtVenta.getGeneratedKeys();
            long ventaId = -1;
            if (rsVenta.next()) {
                ventaId = rsVenta.getLong(1);
            }

            // 2. Insertar cada producto del carrito en la tabla 'linea_de_venta'
            String sqlLinea = "INSERT INTO linea_de_venta (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtLinea = conn.prepareStatement(sqlLinea);

            for (VentaItem item : carrito) {
                pstmtLinea.setLong(1, ventaId);
                pstmtLinea.setString(2, item.getId());
                pstmtLinea.setInt(3, item.getCantidad());
                pstmtLinea.setDouble(4, item.getSubtotal());
                pstmtLinea.addBatch();

                actualizarStock(conn, item.getId(), item.getCantidad());
            }
            pstmtLinea.executeBatch();

            // 3. Si es a cuotas, generar los registros en la tabla 'cuotas'
            if (esEnCuotas) {
                double montoCuota = montoTotalVenta / numCuotas;
                // La consulta se ha modificado para incluir 'fecha_vencimiento'
                String sqlCuota = "INSERT INTO cuotas (venta_id, monto_cuota, fecha_vencimiento, estado, tipo_cuota) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmtCuota = conn.prepareStatement(sqlCuota);

                LocalDate fechaVencimiento = LocalDate.now();
                int intervaloDias = (int) Math.round((double) 200 / numCuotas);

                for (int i = 0; i < numCuotas; i++) {
                    fechaVencimiento = fechaVencimiento.plusDays(intervaloDias);

                    pstmtCuota.setLong(1, ventaId);
                    pstmtCuota.setDouble(2, montoCuota);
                    pstmtCuota.setDate(3, Date.valueOf(fechaVencimiento)); // Se añade la fecha de vencimiento
                    pstmtCuota.setString(4, "Pendiente");
                    pstmtCuota.setString(5, tipoCuota);
                    pstmtCuota.addBatch();
                }
                pstmtCuota.executeBatch();
            }

            conn.commit();
            System.out.println("Venta registrada con éxito. Venta ID: " + ventaId);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error al registrar la venta: " + e.getMessage());
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

    private static void actualizarStock(Connection conn, String productoId, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cantidad);
            pstmt.setString(2, productoId);
            pstmt.executeUpdate();
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

    public static List<Producto> obtenerProductoBajoStock() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos WHERE stock < 5";

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


    public static Cliente obtenerClientePorDni(String dni) {
        String sql = "SELECT * FROM cliente WHERE dni=?";
        Cliente cliente = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("telefono"),
                            rs.getString("dni")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public static void insertarCliente(String nombre, String apellido, String telefono, String dni) {
        // La lista de columnas no incluye 'id' porque es autoincremental.
        // El orden aquí debe ser consistente con la asignación de parámetros.
        String sql = "INSERT INTO cliente (nombre, apellido, telefono, dni) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignación de parámetros
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, dni);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente " + nombre + " " + apellido + " agregado exitosamente.");
            } else {
                System.out.println("Error: No se pudo agregar el cliente.");
            }

        } catch (SQLException e) {
            // Esta es la parte crucial: aquí se mostrará la razón del error.
            System.err.println("Error al insertar cliente en la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<ResumenVenta> obtenerResumenVentas() {
        List<ResumenVenta> resumenVentas = new ArrayList<>();
        String sql = """
            SELECT
                c.venta_id,
                cl.dni AS cliente_dni,
                v.monto_total,
                v.saldo_pendiente AS saldo_restante,
                c.tipo_cuota,
                COUNT(c.id) AS cuotas_totales,
                SUM(CASE WHEN c.estado = 'Pagada' THEN 1 ELSE 0 END) AS cuotas_pagadas,
                SUM(CASE WHEN c.estado = 'Pendiente' THEN 1 ELSE 0 END) AS cuotas_pendientes,
                DATEADD('DAY', 200, v.fecha_compra) AS fecha_plazo_200_dias
            FROM cuotas AS c
            JOIN venta AS v ON c.venta_id = v.id
            JOIN cliente AS cl ON v.cliente_id = cl.id
            GROUP BY c.venta_id, cl.dni, v.monto_total, v.saldo_pendiente, c.tipo_cuota, v.fecha_compra
            ORDER BY c.venta_id ASC;
            """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                resumenVentas.add(new ResumenVenta(
                        rs.getLong("venta_id"),
                        rs.getString("cliente_dni"),
                        rs.getDouble("monto_total"),
                        rs.getDouble("saldo_restante"),
                        rs.getString("tipo_cuota"),
                        rs.getInt("cuotas_totales"),
                        rs.getInt("cuotas_pagadas"),
                        rs.getInt("cuotas_pendientes"),
                        rs.getDate("fecha_plazo_200_dias").toLocalDate()));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el resumen de ventas detallado: " + e.getMessage());
            e.printStackTrace();
        }
        return resumenVentas;
    }

    public static List<ResumenVenta> buscarResumenVentasPorDni(String dni) {
        List<ResumenVenta> resumenEncontrados = new ArrayList<>();
        String sql = """
            SELECT
                c.venta_id,
                cl.dni AS cliente_dni,
                v.monto_total,
                v.saldo_pendiente AS saldo_restante,
                c.tipo_cuota,
                COUNT(c.id) AS cuotas_totales,
                SUM(CASE WHEN c.estado = 'Pagada' THEN 1 ELSE 0 END) AS cuotas_pagadas,
                SUM(CASE WHEN c.estado = 'Pendiente' THEN 1 ELSE 0 END) AS cuotas_pendientes,
                DATEADD('DAY', 200, v.fecha_compra) AS fecha_plazo_200_dias
            FROM cuotas AS c
            JOIN venta AS v ON c.venta_id = v.id
            JOIN cliente AS cl ON v.cliente_id = cl.id
            WHERE cl.dni = ?
            GROUP BY c.venta_id, cl.dni, v.monto_total, v.saldo_pendiente, c.tipo_cuota, v.fecha_compra
            ORDER BY c.venta_id ASC;
            """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                resumenEncontrados.add(new ResumenVenta(
                        rs.getLong("venta_id"),
                        rs.getString("cliente_dni"),
                        rs.getDouble("monto_total"),
                        rs.getDouble("saldo_restante"),
                        rs.getString("tipo_cuota"),
                        rs.getInt("cuotas_totales"),
                        rs.getInt("cuotas_pagadas"),
                        rs.getInt("cuotas_pendientes"),
                        rs.getDate("fecha_plazo_200_dias").toLocalDate()));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar resumen de ventas detallado por DNI: " + e.getMessage());
            e.printStackTrace();
        }
        return resumenEncontrados;
    }

}


