package com.galan.proyectotienda2025;

public class Producto {
    public String id;
    public String nombre;
    public double precioCompra;
    public double precioVenta;
    public String temporada;
    public String promocionable;
    public String descripcion;
    public String marca;
    public int stock;

    public Producto(String id, String nombre, double precioCompra, double precioVenta, String temporada,
                    String promocionable, String descripcion, String marca, int stock) {
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

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public String getMarca() { return marca; }
    public String getTemporada() { return temporada; }
    public int getStock() { return stock; }
    public String getPromocionable() { return promocionable; }


    @Override
    public String toString() {
        return String.format("%s: %s (Compra: $%.2f, Venta: $%.2f) Stock: %d", id, nombre, precioCompra, precioVenta, stock);
    }
}
