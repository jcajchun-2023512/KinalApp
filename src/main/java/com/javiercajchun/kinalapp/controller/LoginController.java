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

    @GetMapping("/")
    public String home (){
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}