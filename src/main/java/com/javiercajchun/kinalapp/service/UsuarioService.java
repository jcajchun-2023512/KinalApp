package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Usuario;
import com.javiercajchun.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){this.usuarioRepository = usuarioRepository; }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarEstadoUsuario() {
        return usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getEstado() == 1)
                .toList();
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        validarUsuario(usuario);
        if(usuario.getEstado() == 0)
            usuario.setEstado(1);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(int id){
        return usuarioRepository.findById((long)id);
    }

    @Override
    public Usuario actualizar(int id, Usuario usuario) {
        if(!usuarioRepository.existsById((long)id)){
            throw new RuntimeException("Usuario no encontrado por ID" + id );
        }
        usuario.setCodigoUsuario((long)id);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar (int id) {
        if(!usuarioRepository.existsById((long)id)){
            throw new RuntimeException("El usuario no se encontro con el ID" + id);
        }
        usuarioRepository.deleteById((long)id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(int id) {
        return usuarioRepository.existsById((long)id);
    }

    private void validarUsuario(Usuario usuario){
        if(usuario == null )
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("El Nombre es un dato obligatorio");
        }

        if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña es un dato obligatorio");
        }

        if(usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("El Email es un dato obligatorio");
        }

        if(usuario.getRol() == null || usuario.getRol().trim().isEmpty()){
            throw new IllegalArgumentException("La Rol es un dato obligatorio");
        }
    }
}
