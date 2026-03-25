package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService{

    List<Usuario> listarTodos();

    List<Usuario> listarEstadoUsuario();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Integer id);

    Usuario actualizar(Integer id, Usuario usuario);

    void eliminar(Integer id);

    boolean existePorId(Integer id);
}
