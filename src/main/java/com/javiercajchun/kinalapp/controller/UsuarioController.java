package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Usuario;
import com.javiercajchun.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // LISTAR TODOS
    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("viewTitle", "Todos los Usuarios");
        return "usuarios";
    }

    // LISTAR ACTIVOS
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("usuarios", usuarioService.listarEstadoUsuario());
        model.addAttribute("viewTitle", "Usuarios Activos");
        return "usuarios";
    }

    // BUSCAR POR ID
    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(value = "id", required = false) Integer id, Model model, RedirectAttributes flash) {
        if (id == null) {
            flash.addFlashAttribute("error", "Debe ingresar un ID para buscar");
            return "redirect:/usuarios";
        }

        var usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuarios", java.util.List.of(usuario.get()));
            model.addAttribute("viewTitle", "Resultado de búsqueda: ID " + id);
        } else {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("error", "No se encontró el usuario con ID: " + id);
            model.addAttribute("viewTitle", "Todos los Usuarios");
        }
        return "usuarios";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("viewTitle", "Registrar Nuevo Usuario");
        return "formularioUsuario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes flash) {
        try {
            if (usuario.getEstado() == 0) {
                usuario.setEstado(1);
            }
            usuarioService.guardar(usuario);
            flash.addFlashAttribute("success", "Usuario guardado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model, RedirectAttributes flash) {
        var usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("viewTitle", "Editar Usuario: " + usuario.get().getUsername());
            return "formularioUsuario";
        } else {
            flash.addFlashAttribute("error", "El usuario no existe");
            return "redirect:/usuarios";
        }
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            if (usuarioService.existePorId(id)) {
                usuarioService.eliminar(id);
                flash.addFlashAttribute("success", "Usuario eliminado con éxito");
            } else {
                flash.addFlashAttribute("error", "No se pudo eliminar, el usuario no existe");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}