package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Producto;
import com.javiercajchun.kinalapp.repository.ProductoRepository;
import com.javiercajchun.kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos(){
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<Producto>> listarEstadoProductos(){
        return ResponseEntity.ok(productoService.listarEstadoProductos());
    }

    @GetMapping("/stock")
    public ResponseEntity<List<String>> listarStock(){
        List<String > stock = productoService.listarStock();
        if (stock.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto>buscarPorId(@PathVariable int id){
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?>  guardar(@RequestBody Producto producto){
        try{
            Producto productoNuevo = productoService.guardar(producto);
            return new ResponseEntity<>(productoNuevo, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id){
        try{
            if(!productoService.existePorId(id)){
                return ResponseEntity.notFound().build();
            }
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Producto producto){
        try{
            if (!productoService.existePorId(id)){
                return ResponseEntity.notFound().build();
            }
            Producto productoActualizar = productoService.actualizar(id, producto);
            return ResponseEntity.ok(productoActualizar);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
