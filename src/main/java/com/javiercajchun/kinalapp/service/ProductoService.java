package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Producto;
import com.javiercajchun.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> listarEstadoProductos(){
        return productoRepository.findAll()
                .stream()
                .filter(producto -> producto.getEstado() == 1)
                .toList();
    }

    @Override
    public List<String> listarStock() {
        return productoRepository.findAll()
                .stream()
                .map(producto -> " Nombre: " + producto.getNombreProducto()+" , " +
                                          " Stock: " + producto.getStock() )
                .toList();
    }

    @Override
    public Producto guardar(Producto producto) {

        validarProducto(producto);
        if(producto.getEstado() == 0)
            producto.setEstado(1);
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(int id) {
        return productoRepository.findById((long)id);
    }

    @Override
    public Producto actualizar(int id, Producto producto) {
        if(!productoRepository.existsById((long)id)){
            throw new RuntimeException("No existe el producto con el id: " + id);
        }
        producto.setCodigoProducto((long)id);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(int id) {
        if(!productoRepository.existsById((long)id)){
            throw new RuntimeException("No existe el producto con el id: " + id);
        }
        productoRepository.deleteById((long)id);
    }

    @Override
    public boolean existePorId(int id) {
        return productoRepository.existsById((long)id);
    }

    private void validarProducto(Producto producto) {
        if(producto == null)
            throw new IllegalArgumentException("El producto no puede ser nulo.");

        if (producto.getNombreProducto() == null)
            throw new IllegalArgumentException("El producto no puede ser nulo.");

        if (producto.getPrecio() == null)
            throw new IllegalArgumentException("El producto no puede ser nulo.");

        if (producto.getStock() <= 0)
            throw new IllegalArgumentException("El producto no puede ser negativo.");
    }

}
