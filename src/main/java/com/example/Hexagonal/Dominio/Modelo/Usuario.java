package com.example.Hexagonal.Dominio.Modelo;

public class Usuario {

    private Long id;
    private String nombre;
    private String correo;

    public Usuario(String nombre, String correo) {
        this(null, nombre, correo);
    }

    public Usuario(Long id, String nombre, String correo) {

        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        else if (correo == null || correo.isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }


    public String getCorreo() {
        return correo;
    }

}
