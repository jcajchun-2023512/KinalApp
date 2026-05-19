package com.javiercajchun.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalleVentas")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_detalleVenta")
    private Long codigoDetalleVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Producto_codigo_producto", referencedColumnName = "codigo_producto",  nullable = false)
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(Long codigoDetalleVenta, int cantidad, BigDecimal precioUnitario, BigDecimal subTotal, Venta venta, Producto producto) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
        this.venta = venta;
        this.producto = producto;
    }

    public Long getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(Long codigoDetalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Venta getVentas() {
        return venta;
    }

    public void setVentas(Venta ventas) {
        this.venta = ventas;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
