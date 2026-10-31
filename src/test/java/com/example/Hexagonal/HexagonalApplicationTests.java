package com.example.Hexagonal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Hexagonal.Aplicacion.Puertos.in.UsuarioUseCase;
import com.example.Hexagonal.Infrastructura.Adaptador.out.UsuarioRepository;

@SpringBootTest
class HexagonalApplicationTests {

	@Autowired
	private UsuarioUseCase usuarioUseCase;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void registraUsuarioEnH2() {
		String correo = UUID.randomUUID() + "@example.test";

		var registrado = usuarioUseCase.registrar("Usuario de prueba", correo);
		assertNotNull(registrado.getId());

		var encontrado = usuarioUseCase.obtenerUsuario(registrado.getId().toString());
		assertNotNull(encontrado);
		assertEquals(registrado.getId(), encontrado.getId());
		assertEquals("Usuario de prueba", encontrado.getNombre());
		assertEquals(correo, encontrado.getCorreo());

		usuarioUseCase.eliminarUsuario(registrado.getId().toString());
		assertTrue(usuarioRepository.findById(registrado.getId()).isEmpty());
	}

	@Test
	void listaUsuariosRegistrados() {
		String correo = UUID.randomUUID() + "@example.test";
		var registrado = usuarioUseCase.registrar("Usuario para listar", correo);

		var usuarios = usuarioUseCase.listarUsuarios();

		assertTrue(usuarios.stream().anyMatch(usuario -> usuario.getId().equals(registrado.getId())));

		usuarioUseCase.eliminarUsuario(registrado.getId().toString());
	}

}
