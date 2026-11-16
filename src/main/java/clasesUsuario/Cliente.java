package clasesUsuario;

import clasesVenta.Venta;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private String telefono;
    private String direccion;
    private List<Venta> historial;

    public Cliente(String nombre, String email, String telefono, String direccion) {
        super(0, nombre, email);
        this.telefono = telefono;
        this.direccion = direccion;
        this.historial = new ArrayList<>();
    }

    public Cliente(int id, String nombre, String email, String telefono, String direccion) {
        super(id, nombre, email);
        this.telefono = telefono;
        this.direccion = direccion;
        this.historial = new ArrayList<>();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void agregarVentaAlHistorial(Venta v) {
        if(v != null) {
            historial.add(v);
        }
    }
    public List<Venta> verHistorial() {
        return new ArrayList<>(historial);
    }

    @Override
    public String getRol() {
        return "Cliente";
    }
}
