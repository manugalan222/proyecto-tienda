package com.galan.proyectotienda2025;

public class Cuotas {
    private final long cuotaId;
    private final long ventaId;
    private final String clienteDni;
    private final double monto;
    private final String fechaVencimiento;
    private final String estado;
    private final String tipoCuota;

    public Cuotas(long cuotaId, long ventaId, String clienteDni, double monto, String fechaVencimiento, String estado, String tipoCuota) {
        this.cuotaId = cuotaId;
        this.ventaId = ventaId;
        this.clienteDni = clienteDni;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.tipoCuota = tipoCuota;
    }

    // Getters para las celdas de la tabla
    public long getCuotaId() { return cuotaId; }
    public long getVentaId() { return ventaId; }
    public String getClienteDni() { return clienteDni; }
    public double getMonto() { return monto; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public String getEstado() { return estado; }
    public String getTipoCuota() { return tipoCuota; }
}

