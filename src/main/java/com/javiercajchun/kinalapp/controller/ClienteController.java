package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Cliente;
import com.javiercajchun.kinalapp.service.IClienteServise;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteServise clienteService;

    public ClienteController(IClienteServise clienteService) {
        this.clienteService = clienteService;
    }

    // 1. LISTAR TODOS
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("viewTitle", "Todos los Clientes");
        return "clientes";
    }

    // 2. LISTAR POR ESTADO (ACTIVOS)
    @GetMapping("/estado")
    public String listarActivos(Model model) {
        model.addAttribute("clientes", clienteService.listarEstadosActivos());
        model.addAttribute("viewTitle", "Clientes Activos");
        return "clientes";
    }

    // 3. BUSCAR POR ID (DPI) PARA VER DETALLES O FILTRAR
    @GetMapping("/buscar")
    public String buscar(@RequestParam("dpi") String dpi, Model model) {
        var cliente = clienteService.buscarPorDPI(dpi);
        if (cliente.isPresent()) {
            model.addAttribute("clientes", cliente.get());
            model.addAttribute("viewTitle", "Resultado de Búsqueda");
        } else {
            model.addAttribute("error", "No se encontró el cliente con DPI: " + dpi);
        }
        return "clientes";
    }

    // 4. MOSTRAR FORMULARIO DE REGISTRO (NUEVO)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente()); // Objeto vacío para el form
        model.addAttribute("viewTitle", "Registrar Nuevo Cliente");
        return "formulario"; // Retorna formulario.html
    }

    // 5. GUARDAR / ACTUALIZAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes flash) {
        try {
            clienteService.guardar(cliente);
            flash.addFlashAttribute("success", "Cliente guardado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    // 6. MOSTRAR FORMULARIO DE EDICIÓN (ACTUALIZAR)
    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable String dpi, Model model, RedirectAttributes flash) {
        var cliente = clienteService.buscarPorDPI(dpi);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("viewTitle", "Editar Cliente: " + cliente.get().getNombreCliente());
            return "formulario";
        } else {
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/clientes";
        }
    }

    // 7. ELIMINAR
    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi, RedirectAttributes flash) {
        if (clienteService.existeDPI(dpi)) {
            clienteService.eliminar(dpi);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito");
        } else {
            flash.addFlashAttribute("error", "No se pudo eliminar, el DPI no existe");
        }
        return "redirect:/clientes";
    }
}