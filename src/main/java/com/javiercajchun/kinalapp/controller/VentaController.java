package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Venta;
import com.javiercajchun.kinalapp.service.IClienteServise;
import com.javiercajchun.kinalapp.service.IProductoService;
import com.javiercajchun.kinalapp.service.IUsuarioService;
import com.javiercajchun.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteServise clienteService;
    private final IProductoService productoService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService,
                           IClienteServise clienteService,
                           IProductoService productoService,
                           IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    // LISTAR TODAS LAS VENTAS
    @GetMapping
    public String listarTodos(Model model) {
        List<Venta> ventas = ventaService.listarTodos();
        model.addAttribute("ventas", ventas);
        
        // Calcular total general de ventas
        BigDecimal totalGeneral = BigDecimal.ZERO;
        if (ventas != null) {
            for (Venta venta : ventas) {
                if (venta.getTotal() != null) {
                    totalGeneral = totalGeneral.add(venta.getTotal());
                }
            }
        }
        model.addAttribute("totalGeneral", totalGeneral);
        model.addAttribute("viewTitle", "Historial de Ventas");
        return "ventas";
    }

    // FORMULARIO NUEVA VENTA
    @GetMapping("/nueva")
    public String mostrarFormularioNuevo(Model model) {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setFechaVenta(LocalDate.now());
        nuevaVenta.setEstado(1);

        model.addAttribute("venta", nuevaVenta);
        model.addAttribute("clientes", clienteService.listarEstadosActivos());
        model.addAttribute("productos", productoService.listarEstadoProductos());
        model.addAttribute("usuarios", usuarioService.listarEstadoUsuario());
        model.addAttribute("viewTitle", "Registrar Nueva Venta");
        return "formularioVenta";
    }

    // GUARDAR VENTA
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta, RedirectAttributes flash, HttpSession session) {
        try {
            if (venta.getFechaVenta() == null) {
                venta.setFechaVenta(LocalDate.now());
            }
            if (venta.getEstado() == 0) {
                venta.setEstado(1);
            }
            ventaService.guardar(venta);
            flash.addFlashAttribute("success", "Venta registrada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    // VER DETALLE DE VENTA
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable int id, Model model, RedirectAttributes flash) {
        System.out.println("=== VER DETALLE VENTA ===");
        System.out.println("ID Venta: " + id);

        var venta = ventaService.buscarPorId(id);
        if (venta.isPresent()) {
            Venta ventaEncontrada = venta.get();
            System.out.println("Venta encontrada: " + ventaEncontrada.getCodigoVenta());
            System.out.println("Detalles: " + ventaEncontrada.getDetalleVentas());

            model.addAttribute("venta", ventaEncontrada);
            model.addAttribute("detalles", ventaEncontrada.getDetalleVentas());
            model.addAttribute("viewTitle", "Detalle de Venta #" + id);
            return "detalleVenta";
        } else {
            System.out.println("Venta NO encontrada con ID: " + id);
            flash.addFlashAttribute("error", "La venta no existe");
            return "redirect:/ventas";
        }
    }

    // ELIMINAR VENTA
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            if (ventaService.existePorId(id)) {
                // En lugar de eliminar, cambiar estado a 0 (anulado)
                var venta = ventaService.buscarPorId(id);
                if (venta.isPresent()) {
                    Venta ventaActualizar = venta.get();
                    ventaActualizar.setEstado(0);
                    ventaService.actualizar(id, ventaActualizar);
                    flash.addFlashAttribute("success", "Venta anulada correctamente");
                }
            } else {
                flash.addFlashAttribute("error", "La venta no existe");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al anular venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }
}