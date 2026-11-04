package com.example.Hexagonal.Aplicacion.Servicio;

import com.example.Hexagonal.Aplicacion.Puertos.in.UsuarioUseCase;
import com.example.Hexagonal.Aplicacion.Puertos.out.UsuarioRepositoryPort;
import com.example.Hexagonal.Dominio.Modelo.Usuario;
import java.util.List;

public class UsuarioService implements UsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(String nombre, String correo) {
        Usuario usuario = new Usuario(nombre, correo);
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    @Override
    public Usuario actualizarUsuario(Long id, String nombre, String correo) {
        return usuarioRepository.actualizar(new Usuario(id, nombre, correo));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.eliminar(id);
    }


}
