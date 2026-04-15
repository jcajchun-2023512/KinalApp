package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Usuario;
import com.javiercajchun.kinalapp.service.IClienteServise;
import com.javiercajchun.kinalapp.service.IProductoService;
import com.javiercajchun.kinalapp.service.IVentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final IClienteServise clienteService;
    private final IProductoService productoService;
    private final IVentaService ventaService;

    public DashboardController(IClienteServise clienteService,
                               IProductoService productoService,
                               IVentaService ventaService) {
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.ventaService = ventaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        System.out.println("=== DASHBOARD CONTROLLER ===");

        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        System.out.println("Sesión usuarioLogueado: " + usuarioLogueado);

        if (usuarioLogueado == null) {
            System.out.println("No hay sesión, redirigiendo a login");
            return "redirect:/login";
        }

        var todosClientes = clienteService.listarTodos();
        var clientesActivos = clienteService.listarEstadosActivos();
        var productos = productoService.listarTodos();
        var ventas = ventaService.listarTodos();

        model.addAttribute("totalClientes", todosClientes.size());
        model.addAttribute("clientesActivos", clientesActivos.size());
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("totalVentas", ventas.size());

        model.addAttribute("ultimosClientes",
                todosClientes.stream().limit(5).toList());

        // Si es Usuario, mostrar el nombre
        if (usuarioLogueado instanceof Usuario) {
            Usuario usuario = (Usuario) usuarioLogueado;
            model.addAttribute("nombreUsuario", usuario.getUsername());
        }

        return "dashboard";
    }
}