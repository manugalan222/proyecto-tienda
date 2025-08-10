package com.galan.proyectotienda2025.modelo;

import com.galan.proyectotienda2025.Database;

public class ProductosModelo {
    Database db;

    public void agregarProductoBd(String temporadaValor, String tipoValor, String marcaValor, int stockValor,
                                  Double precioCompra, Double precioVenta, String productoId,
                                  String productoNombre, String productoDesc, Boolean promoValor) {
        Database.insertarProducto(productoId, productoNombre, precioCompra, precioVenta, temporadaValor, promoValor, productoDesc, marcaValor,stockValor);
        //Database.listarProductos();

    }
}
