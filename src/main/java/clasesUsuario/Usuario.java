package clasesUsuario;

import interfaz.Identificable;

public abstract class Usuario implements Identificable {
    private int id;
    private String nombre;
    private String email;

    protected Usuario(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public abstract String getRol();

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " <" + email + "> (" + getRol() + ")";
    }
}
