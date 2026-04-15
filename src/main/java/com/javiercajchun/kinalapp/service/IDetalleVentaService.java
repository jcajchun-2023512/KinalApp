package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> listarVentas();

    List<DetalleVenta> listarEstado();

    DetalleVenta guardar (DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorId (int id);

    boolean existePorId (int id);
}
