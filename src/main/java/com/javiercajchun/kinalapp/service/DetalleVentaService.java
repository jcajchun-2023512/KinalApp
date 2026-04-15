package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.DetalleVenta;
import com.javiercajchun.kinalapp.entity.Producto;
import com.javiercajchun.kinalapp.entity.Venta;
import com.javiercajchun.kinalapp.repository.DetalleVentaRepository;
import com.javiercajchun.kinalapp.repository.ProductoRepository;
import com.javiercajchun.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
                               VentaRepository ventaRepository,
                               ProductoRepository productoRepository){
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarVentas() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarEstado() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        Venta ventaReal = ventaRepository.findById(detalleVenta.getVentas().getCodigoVenta()).orElse(null);
        Producto productoReal = productoRepository.findById(detalleVenta.getProducto().getCodigoProducto()).orElse(null);

        detalleVenta.setVentas(ventaReal);
        detalleVenta.setProducto(productoReal);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional
    public Optional<DetalleVenta> buscarPorId(int id) {
        return detalleVentaRepository.findById((long) id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(int id) {
        return detalleVentaRepository.existsById((long)id);

    }

    }
