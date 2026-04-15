package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Cliente;
import com.javiercajchun.kinalapp.entity.Usuario;
import com.javiercajchun.kinalapp.entity.Venta;
import com.javiercajchun.kinalapp.repository.ClienteRepository;
import com.javiercajchun.kinalapp.repository.UsuarioRepository;
import com.javiercajchun.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public VentaService(VentaRepository ventaRepository,  ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> listarEstadoVenta() {
        return ventaRepository.findAll();

    }

    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        Cliente clienteReal = clienteRepository.findById(venta.getCliente().getDPICliente()).orElse(null);
        Usuario usuarioReal = usuarioRepository.findById(venta.getUsuario().getCodigoUsuario()).orElse(null);

        venta.setCliente(clienteReal);
        venta.setUsuario(usuarioReal);

        if (venta.getEstado() == 0) {
            venta.setEstado(1);
        }

        return ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public Optional<Venta> buscarPorId(int id) {

        return ventaRepository.findById((long) id);
    }

    @Override
    @Transactional

    public Venta actualizar(int id, Venta venta) {
        if (!ventaRepository.existsById((long) id)){
            throw new RuntimeException("Venta no encontrada por ID" + id);
        }

        venta.setCodigoVenta((long) id);
        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(int id) {
        return ventaRepository.existsById((long)id);
    }

    private void validarVenta(Venta venta){
        if (venta == null)
            throw new IllegalArgumentException("La venta no puede ser nula");

        if (venta.getFechaVenta() == null)
            throw new IllegalArgumentException("La fecha no puede ser nula");

        if (venta.getTotal() == null)
            throw new IllegalArgumentException("El total no puede ser nulo");
    }
}
