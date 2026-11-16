package clasesUsuario;

public class Cliente extends Usuario {

    private String telefono;
    private String direccion;

    public Cliente(String nombre, String email, String telefono, String direccion) {
        super(0, nombre, email);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Override
    public String getRol() {
        return "Cliente";
    }
}
