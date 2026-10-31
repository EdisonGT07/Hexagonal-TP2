package com.example.Hexagonal.Aplicacion.Puertos.in;

import com.example.Hexagonal.Dominio.Modelo.Usuario;
import java.util.List;

public interface UsuarioUseCase {
    Usuario registrar(String nombre, String correo);
    Usuario obtenerUsuario(String id);
    List<Usuario> listarUsuarios();
    void eliminarUsuario(String id);
}
