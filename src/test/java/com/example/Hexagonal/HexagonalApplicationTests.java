package com.example.Hexagonal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Hexagonal.Aplicacion.Puertos.in.UsuarioUseCase;
import com.example.Hexagonal.Infrastructura.Adaptador.out.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
class HexagonalApplicationTests {

	@Autowired
	private UsuarioUseCase usuarioUseCase;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void registraUsuarioEnH2() {
		String correo = UUID.randomUUID() + "@example.test";

		var registrado = usuarioUseCase.registrar("Usuario de prueba", correo);
		assertNotNull(registrado.getId());

		var encontrado = usuarioUseCase.obtenerUsuario(registrado.getId());
		assertNotNull(encontrado);
		assertEquals(registrado.getId(), encontrado.getId());
		assertEquals("Usuario de prueba", encontrado.getNombre());
		assertEquals(correo, encontrado.getCorreo());

		usuarioUseCase.eliminarUsuario(registrado.getId());
		assertTrue(usuarioRepository.findById(registrado.getId()).isEmpty());
	}

	@Test
	void listaUsuariosRegistrados() {
		String correo = UUID.randomUUID() + "@example.test";
		var registrado = usuarioUseCase.registrar("Usuario para listar", correo);

		var usuarios = usuarioUseCase.listarUsuarios();

		assertTrue(usuarios.stream().anyMatch(usuario -> usuario.getId().equals(registrado.getId())));

		usuarioUseCase.eliminarUsuario(registrado.getId());
	}

	@Test
	void actualizaUsuarioRegistrado() {
		var registrado = usuarioUseCase.registrar("Nombre anterior", UUID.randomUUID() + "@example.test");

		var actualizado = usuarioUseCase.actualizarUsuario(
				registrado.getId(), "Nombre actualizado", "actualizado@example.test");

		assertNotNull(actualizado);
		assertEquals(registrado.getId(), actualizado.getId());
		assertEquals("Nombre actualizado", actualizado.getNombre());
		assertEquals("actualizado@example.test", actualizado.getCorreo());
		assertEquals("Nombre actualizado", usuarioUseCase.obtenerUsuario(registrado.getId()).getNombre());

		usuarioUseCase.eliminarUsuario(registrado.getId());
		assertTrue(usuarioRepository.findById(registrado.getId()).isEmpty());
	}

	@Test
	void completaCrudDesdeLaInterfazWeb() throws Exception {
		String correoInicial = UUID.randomUUID() + "@example.test";
		String correoActualizado = UUID.randomUUID() + "@example.test";

		mockMvc.perform(post("/usuarios/registrar")
				.param("nombre", "Usuario web")
				.param("correo", correoInicial))
				.andExpect(status().is3xxRedirection());

		var registrado = usuarioRepository.findAll().stream()
				.filter(usuario -> usuario.getCorreo().equals(correoInicial))
				.findFirst()
				.orElseThrow();
		String id = registrado.getId().toString();

		var pagina = mockMvc.perform(get("/usuarios/nuevo"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(pagina.contains("Usuario web"));
		assertTrue(pagina.contains(correoInicial));

		var busqueda = mockMvc.perform(get("/usuarios/buscar").param("id", id))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(busqueda.contains("RESULTADO DE BÚSQUEDA"));

		var edicion = mockMvc.perform(get("/usuarios/editar").param("id", id))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(edicion.contains("ACTUALIZAR REGISTRO"));

		mockMvc.perform(post("/usuarios/actualizar")
				.param("id", id)
				.param("nombre", "Usuario actualizado")
				.param("correo", correoActualizado))
				.andExpect(status().is3xxRedirection());

		var paginaActualizada = mockMvc.perform(get("/usuarios/nuevo"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(paginaActualizada.contains("Usuario actualizado"));
		assertTrue(paginaActualizada.contains(correoActualizado));

		mockMvc.perform(post("/usuarios/eliminar").param("id", id))
				.andExpect(status().is3xxRedirection());
		assertTrue(usuarioRepository.findById(registrado.getId()).isEmpty());
	}

}
