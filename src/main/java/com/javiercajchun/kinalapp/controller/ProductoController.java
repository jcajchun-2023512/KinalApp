package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Producto;
import com.javiercajchun.kinalapp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // LISTAR TODOS
    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("viewTitle", "Todos los Productos");
        return "productos";
    }

    // LISTAR ACTIVOS
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("productos", productoService.listarEstadoProductos());
        model.addAttribute("viewTitle", "Productos Activos");
        return "productos";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("viewTitle", "Registrar Nuevo Producto");
        return "formularioProducto";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes flash) {
        try {
            if (producto.getEstado() == 0) {
                producto.setEstado(1);
            }
            productoService.guardar(producto);
            flash.addFlashAttribute("success", "Producto guardado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model, RedirectAttributes flash) {
        var producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("viewTitle", "Editar Producto: " + producto.get().getNombreProducto());
            return "formularioProducto";
        } else {
            flash.addFlashAttribute("error", "El producto no existe");
            return "redirect:/productos";
        }
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            if (productoService.existePorId(id)) {
                productoService.eliminar(id);
                flash.addFlashAttribute("success", "Producto eliminado con éxito");
            } else {
                flash.addFlashAttribute("error", "No se pudo eliminar, el producto no existe");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}