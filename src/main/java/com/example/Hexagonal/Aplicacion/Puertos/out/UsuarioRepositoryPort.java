package com.example.Hexagonal.Aplicacion.Puertos.out;

import java.util.List;

import com.example.Hexagonal.Dominio.Modelo.Usuario;

public interface UsuarioRepositoryPort{
    Usuario guardar(Usuario usuario);
    Usuario buscarPorId(String id);
    List<Usuario> listarTodos();
    void eliminar(Long id);

}