package com.example.Hexagonal.Infrastructura.Adaptador.out;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.Hexagonal.Aplicacion.Puertos.out.UsuarioRepositoryPort;
import com.example.Hexagonal.Dominio.Modelo.Usuario;

@Component
public class UsuarioAdapter implements UsuarioRepositoryPort {
    private final UsuarioRepository usuarioRepository;

    public UsuarioAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity usuarioEntity = new UsuarioEntity(usuario.getNombre(), usuario.getCorreo());
        UsuarioEntity guardado = usuarioRepository.save(usuarioEntity);
        return new Usuario(guardado.getId(), guardado.getNombre(), guardado.getCorreo());
    }

    @Override
    public Usuario buscarPorId(String id) {
        Long usuarioId = Long.parseLong(id);
        return usuarioRepository.findById(usuarioId)
                .map(entity -> new Usuario(entity.getId(), entity.getNombre(), entity.getCorreo()))
                .orElse(null);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(entity -> new Usuario(entity.getId(), entity.getNombre(), entity.getCorreo()))
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

}
