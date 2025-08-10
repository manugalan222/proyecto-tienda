package com.galan.proyectotienda2025.controlador;

import com.galan.proyectotienda2025.Database;

public class VentaItem {
    private Database.Producto producto;
    private int cantidad;

    public VentaItem(Database.Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Database.Producto getProducto() {
        return producto;
    }

    public String getId() {
        return producto.id;
    }

    public String getNombre() {
        return producto.nombre;
    }

    public double getPrecio() {
        return producto.precioVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.precioVenta * cantidad;
    }
}
