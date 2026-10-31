package com.example.Hexagonal.Infrastructura.Adaptador.in;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Hexagonal.Aplicacion.Puertos.in.UsuarioUseCase;

@Controller
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping({"/usuarios/nuevo", "/usuario/nuevo"})
    public String mostrarFormularioRegistro(Model model) {
        return mostrarFormulario(model);
    }

    @PostMapping({"/usuarios/registrar", "/usuarios/guardar", "/usuario/registrar", "/usuario/guardar"})
    public String registrarUsuario(
            @ModelAttribute UsuarioRequest usuarioRequest,
            RedirectAttributes redirectAttributes) {
        var usuario = usuarioUseCase.registrar(usuarioRequest.getNombre(), usuarioRequest.getCorreo());
        redirectAttributes.addFlashAttribute(
                "mensaje", "Usuario registrado correctamente. Su ID es " + usuario.getId() + ".");
        return "redirect:/usuarios/nuevo";
    }

    @GetMapping({"/", "/usuarios", "/usuario"})
    public String paginaPrincipal() {
        return "redirect:/usuarios/nuevo";
    }

    @GetMapping({"/usuarios/buscar", "/usuario/buscar"})
    public String buscarUsuario(@RequestParam Long id, Model model) {
        mostrarFormulario(model);
        if (id <= 0) {
            model.addAttribute("error", "El ID debe ser mayor que cero.");
            return "usuario";
        }

        var usuario = usuarioUseCase.obtenerUsuario(id.toString());
        if (usuario == null) {
            model.addAttribute("error", "No se encontró un usuario con ese ID.");
        } else {
            model.addAttribute("usuarioEncontrado", usuario);
        }
        return "usuario";
    }

    @PostMapping({"/usuarios/eliminar", "/usuario/eliminar"})
    public String eliminarUsuario(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (id <= 0) {
            redirectAttributes.addFlashAttribute("error", "El ID debe ser mayor que cero.");
            return "redirect:/usuarios/nuevo";
        }
        if (usuarioUseCase.obtenerUsuario(id.toString()) == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró un usuario con ese ID.");
            return "redirect:/usuarios/nuevo";
        }

        usuarioUseCase.eliminarUsuario(id.toString());
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        return "redirect:/usuarios/nuevo";
    }

    private String mostrarFormulario(Model model) {
        model.addAttribute("usuarioRequest", new UsuarioRequest());
        var usuarios = usuarioUseCase.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "usuario";
    }
}