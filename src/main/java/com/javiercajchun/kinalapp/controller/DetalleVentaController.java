package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.DetalleVenta;
import com.javiercajchun.kinalapp.entity.Venta;
import com.javiercajchun.kinalapp.service.IDetalleVentaService;
import com.javiercajchun.kinalapp.service.IProductoService;
import com.javiercajchun.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/detalleVentas")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService,
                                  IVentaService ventaService,
                                  IProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    // Listar todos los detalles
    @GetMapping
    public String listarTodos(Model model) {
        List<DetalleVenta> detalles = detalleVentaService.listarVentas();
        model.addAttribute("detalles", detalles != null ? detalles : new ArrayList<>());
        
        // Calcular total general
        BigDecimal totalGeneral = BigDecimal.ZERO;
        if (detalles != null) {
            for (DetalleVenta detalle : detalles) {
                if (detalle.getSubTotal() != null) {
                    totalGeneral = totalGeneral.add(detalle.getSubTotal());
                }
            }
        }
        model.addAttribute("totalGeneral", totalGeneral);
        model.addAttribute("viewTitle", "Detalle de Ventas");
        return "detalleVentas";
    }

    // Ver detalles de una venta específica
    @GetMapping("/venta/{ventaId}")
    public String verDetallesPorVenta(@PathVariable int ventaId, Model model, RedirectAttributes flash) {
        var venta = ventaService.buscarPorId(ventaId);
        if (venta.isPresent()) {
            Venta ventaEncontrada = venta.get();
            model.addAttribute("venta", ventaEncontrada);
            model.addAttribute("detalles", ventaEncontrada.getDetalleVentas());
            model.addAttribute("viewTitle", "Detalles de Venta #" + ventaId);
            return "detalleVenta";
        } else {
            flash.addFlashAttribute("error", "La venta no existe");
            return "redirect:/detalleVentas";
        }
    }

    // Formulario para agregar detalle a una venta
    @GetMapping("/nuevo/{ventaId}")
    public String mostrarFormularioNuevo(@PathVariable int ventaId, Model model, RedirectAttributes flash) {
        var venta = ventaService.buscarPorId(ventaId);
        if (venta.isPresent()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVentas(venta.get());
            detalle.setCantidad(1);

            model.addAttribute("detalle", detalle);
            model.addAttribute("productos", productoService.listarEstadoProductos());
            model.addAttribute("ventaId", ventaId);
            model.addAttribute("viewTitle", "Agregar Producto a Venta #" + ventaId);
            return "formularioDetalleVenta";
        } else {
            flash.addFlashAttribute("error", "La venta no existe");
            return "redirect:/detalleVentas";
        }
    }

    // Guardar detalle
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DetalleVenta detalle,
                          @RequestParam int ventaId,
                          @RequestParam Long productoId,
                          RedirectAttributes flash) {
        try {
            // Obtener la venta
            var venta = ventaService.buscarPorId(ventaId);
            if (!venta.isPresent()) {
                flash.addFlashAttribute("error", "La venta no existe");
                return "redirect:/detalleVentas";
            }

            // Obtener el producto
            var producto = productoService.buscarPorId(productoId.intValue());
            if (!producto.isPresent()) {
                flash.addFlashAttribute("error", "El producto no existe");
                return "redirect:/detalleVentas/venta/" + ventaId;
            }

            // Calcular subtotal
            BigDecimal precioUnitario = producto.get().getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubTotal(subtotal);
            detalle.setVentas(venta.get());
            detalle.setProducto(producto.get());

            // Guardar detalle
            detalleVentaService.guardar(detalle);

            // Actualizar el total de la venta
            Venta ventaActualizar = venta.get();
            BigDecimal totalActual = ventaActualizar.getTotal() != null ? ventaActualizar.getTotal() : BigDecimal.ZERO;
            BigDecimal nuevoTotal = totalActual.add(subtotal);
            ventaActualizar.setTotal(nuevoTotal);
            ventaService.actualizar(ventaId, ventaActualizar);

            flash.addFlashAttribute("success", "Producto agregado correctamente a la venta");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/detalleVentas/venta/" + ventaId;
    }

}