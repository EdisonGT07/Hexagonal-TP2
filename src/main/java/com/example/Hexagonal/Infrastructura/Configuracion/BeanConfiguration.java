package com.example.Hexagonal.Infrastructura.Configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Hexagonal.Aplicacion.Puertos.out.UsuarioRepositoryPort;
import com.example.Hexagonal.Aplicacion.Servicio.UsuarioService;

@Configuration
public class BeanConfiguration {

    @Bean
    public UsuarioService usuarioService(UsuarioRepositoryPort usuarioRepository) {
        return new UsuarioService(usuarioRepository);
    }

}
