package com.galan.proyectotienda2025;

import java.time.LocalDate;

public class ResumenVenta {
    private final long ventaId;
    private final String clienteDni;
    private final double montoTotal;
    private final double saldoRestante;
    private final String tipoCuota;
    private final int cuotasTotales;
    private final int cuotasPagadas;
    private final int cuotasPendientes;
    private final LocalDate fechaPlazo200Dias;

    public ResumenVenta(long ventaId, String clienteDni, double montoTotal, double saldoRestante,
                                 String tipoCuota, int cuotasTotales, int cuotasPagadas, int cuotasPendientes,
                                 LocalDate fechaPlazo200Dias) {
        this.ventaId = ventaId;
        this.clienteDni = clienteDni;
        this.montoTotal = montoTotal;
        this.saldoRestante = saldoRestante;
        this.tipoCuota = tipoCuota;
        this.cuotasTotales = cuotasTotales;
        this.cuotasPagadas = cuotasPagadas;
        this.cuotasPendientes = cuotasPendientes;
        this.fechaPlazo200Dias = fechaPlazo200Dias;
    }

    // Getters para acceder a los datos.
    // Estos métodos son esenciales para que JavaFX pueda enlazar los datos
    // a las columnas de la TableView.
    public long getVentaId() { return ventaId; }
    public String getClienteDni() { return clienteDni; }
    public double getMontoTotal() { return montoTotal; }
    public double getSaldoRestante() { return saldoRestante; }
    public String getTipoCuota() { return tipoCuota; }
    public int getCuotasTotales() { return cuotasTotales; }
    public int getCuotasPagadas() { return cuotasPagadas; }
    public int getCuotasPendientes() { return cuotasPendientes; }
    public LocalDate getFechaPlazo200Dias() { return fechaPlazo200Dias; }
}