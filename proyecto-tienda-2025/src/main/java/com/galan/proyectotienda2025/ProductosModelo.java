package com.galan.proyectotienda2025;

public class ProductosModelo {
    Database db;

    public void agregarProductoBd(String temporadaValor, String marcaValor, int stockValor,
                                  Double precioCompra, Double precioVenta, String productoId,
                                  String productoNombre, String productoDesc, String promoValor) {
        Database.insertarProducto(productoId, productoNombre, precioCompra, precioVenta, temporadaValor, promoValor, productoDesc, marcaValor,stockValor);
        //Database.listarProductos();
        System.out.println("Nashe");
    }

}
