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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " <" + email + "> (" + getRol() + ")";
    }
}
