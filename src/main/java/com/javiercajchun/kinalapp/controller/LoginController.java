package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Usuario;
import com.javiercajchun.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        System.out.println("GET /login - Mostrando formulario");
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        System.out.println("POST /login - Procesando login");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        // Buscar usuario por email (cambiamos Cliente por Usuario)
        var usuarioOptional = usuarioService.buscarPorEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Verificar contraseña (en un sistema real, deberías usar encriptación)
            if (usuario.getPassword().equals(password) && usuario.getEstado() == 1) {
                // Guardar usuario en sesión
                session.setAttribute("usuarioLogueado", usuario);
                System.out.println("Sesión guardada: " + session.getAttribute("usuarioLogueado"));
                System.out.println("Redirigiendo a dashboard");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta o usuario inactivo");
                return "redirect:/login";
            }
        } else {
            // Si no existe usuario, crear uno de demostración (solo para desarrollo)
            // En producción, esto debería eliminarse
            Usuario usuarioDemo = new Usuario();
            usuarioDemo.setCodigoUsuario(1L);
            usuarioDemo.setUsername("admin");
            usuarioDemo.setEmail("admin@kinal.edu.gt");
            usuarioDemo.setPassword("admin123");
            usuarioDemo.setRol("ADMIN");
            usuarioDemo.setEstado(1);

            session.setAttribute("usuarioLogueado", usuarioDemo);
            System.out.println("Usuario demo creado y guardado en sesión");
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        System.out.println("GET /register - Mostrando registro");
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String nombre,
                                   @RequestParam String apellido,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("POST /register - Procesando registro");

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(nombre + " " + apellido);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setRol("USER");
        nuevoUsuario.setEstado(1);

        try {
            usuarioService.guardar(nuevoUsuario);
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("GET /logout - Cerrando sesión");
        session.invalidate();
        return "redirect:/login";
    }
}