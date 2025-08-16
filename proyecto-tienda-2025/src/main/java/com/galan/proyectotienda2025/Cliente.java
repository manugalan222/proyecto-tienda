package com.galan.proyectotienda2025;

public class Cliente {
    public long id;
    public String nombre;
    public String apellido;
    public String telefono;
    public String dni;

    public Cliente(long id, String nombre, String apellido, String telefono, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.dni = dni;
    }

    public long getId() {
        return id;
    }
    public String getNombre(){ return nombre; }
    public String getApellido(){ return apellido;}
    public String getTelefono(){return telefono;}
    public String getDni(){ return dni;}
}
