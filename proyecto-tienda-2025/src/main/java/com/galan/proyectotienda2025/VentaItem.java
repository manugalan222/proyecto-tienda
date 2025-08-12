package com.galan.proyectotienda2025;

public class VentaItem {
    private Producto producto;
    private int cantidad;

    public VentaItem(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
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
