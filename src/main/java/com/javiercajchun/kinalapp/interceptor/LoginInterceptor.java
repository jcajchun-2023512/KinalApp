package com.javiercajchun.kinalapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        if (uri.equals("/login") || uri.equals("/register") || uri.startsWith("/css") ||
                uri.startsWith("/js") || uri.equals("/") || uri.startsWith("/acceso-denegado")) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}