package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Venta;
import com.javiercajchun.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepostory;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepostory = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepostory.findAll();
    }

    @Override
    public List<Venta> listarEstadoVenta() {
        return ventaRepostory.findAll();

    }

    @Override
    public Venta guardar(Venta venta) {

        validarVenta(venta);
        if(venta.getEstado() == 0)
            venta.setEstado(1);
        return ventaRepostory.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorId(int id) {
        return ventaRepostory.findById(id);
    }

    @Override
    public Venta actualizar(int id, Venta venta) {

        if (!ventaRepostory.existsById(id)){
            throw new RuntimeException("Venta no encontrada por ID" + id);
        }

        venta.setCodigoVenta(id);
        validarVenta(venta);

        return ventaRepostory.save(venta);
    }

    @Override
    public void eliminar(int id) {
        if (!ventaRepostory.existsById(id)){
            throw new RuntimeException("Venta no encontrada por ID" + id);
        }
        ventaRepostory.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(int id) {
        return ventaRepostory.existsById(id);
    }

    private void validarVenta(Venta venta){
        if (venta == null)
            throw new IllegalArgumentException("La venta no puede ser nula");

        if (venta.getFechaVenta() == null)
            throw new IllegalArgumentException("La fecha no puede ser nula");

        if (venta.getTotal() == 0)
            throw new IllegalArgumentException("El total no puede ser nulo");

        if (venta.getEstado() > 1)
            throw new IllegalArgumentException("El estado no puede ser mayor a 1");
    }
}
