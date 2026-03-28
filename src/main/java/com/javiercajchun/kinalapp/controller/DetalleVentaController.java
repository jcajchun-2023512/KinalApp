package com.javiercajchun.kinalapp.controller;


import com.javiercajchun.kinalapp.entity.DetalleVenta;
import com.javiercajchun.kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Detalle")
public class DetalleVentaController {

    private IDetalleVentaService detalleVentaService;
    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    ResponseEntity<List<DetalleVenta>> listar() {
        List<DetalleVenta> Detalleventas = detalleVentaService.listarTodos();
        return ResponseEntity.ok(Detalleventas);
    }

    @GetMapping("/{codigoDetalleVenta}")
    public ResponseEntity<DetalleVenta> buscarPorId(@PathVariable long codigoDetalleVenta) {
        return detalleVentaService.buscarPorId(codigoDetalleVenta)
                .map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta detalleVentasNueva = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(detalleVentasNueva, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{codigoDetalleVenta}")
    public ResponseEntity<?> actualizar(@PathVariable long codigoDetalleVenta, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorId(codigoDetalleVenta)) {
                return ResponseEntity.notFound().build();
            }
            DetalleVenta DetalleVentasActualizada = detalleVentaService.actualizar(codigoDetalleVenta, detalleVenta);
            return ResponseEntity.ok(DetalleVentasActualizada);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigoDetalleVenta}")
    public ResponseEntity<Void> eliminar(@PathVariable long codigoDetalleVenta){
        try{
            if(!detalleVentaService.existePorId(codigoDetalleVenta)){
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(codigoDetalleVenta);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
