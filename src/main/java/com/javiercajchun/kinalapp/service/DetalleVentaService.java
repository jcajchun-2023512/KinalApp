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
public class DetalleVentaService implements IDetalleVentaService{

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,  VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        Venta ventaReal = ventaRepository.findById(detalleVenta.getVentas().getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        Producto productoReal = productoRepository.findById(detalleVenta.getProducto().getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrada"));

        detalleVenta.setVentas(ventaReal);
        detalleVenta.setProducto(productoReal);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional
    public Optional<DetalleVenta> buscarPorId(Long id) {
        return detalleVentaRepository.findById( id);
    }

    @Override
    @Transactional
    public DetalleVenta actualizar(Long id, DetalleVenta detalleVenta) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("No existe el detalle de venta con el id: " + id);
        }

        detalleVenta.setCodigoDetalleVenta(id);
        validarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!detalleVentaRepository.existsById( id)) {
            throw new RuntimeException("Detalle de venta no existe" + id);
        }
        detalleVentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return detalleVentaRepository.existsById( id);
    }

    private void validarDetalleVenta(DetalleVenta detalleVenta) {

        if (detalleVenta.getCantidad() < 0)
            throw new IllegalArgumentException("La cantidad no debe de ser nulo");

        if (detalleVenta.getPrecioUnitario() == null)
            throw new IllegalArgumentException("La precio no debe de ser nulo");

        if (detalleVenta.getSubTotal() == null)
            throw new IllegalArgumentException("La subtotal no debe de ser nulo");
    }
}
