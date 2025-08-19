package com.galan.proyectotienda2025;

public class LineaDeVenta {
    private String nombreProducto;
    private int cantidad;
    private double subtotal;

    public LineaDeVenta(String nombreProducto, int cantidad, double subtotal) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters necesarios para PropertyValueFactory
    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
