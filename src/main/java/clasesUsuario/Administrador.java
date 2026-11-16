package clasesUsuario;

import clasesProducto.Producto;
import clasesVenta.VentaADomicilio;
import enums.EstadoEnvio;
import excepciones.PrecioInvalidoException;
import excepciones.VentaInvalidaException;

public class Administrador extends Usuario {

    private final String clave;

    public Administrador(int id, String nombre, String email, String clave) {
        super(id,nombre,email);
        this.clave = clave;

    }

    @Override
    public String getRol() {
        return "Administrador";
    }
    public void agregarProducto(Producto p) {
        if (p == null) {
            throw new IllegalArgumentException("Hay que indicar un producto");
        }
    }

    public void actualizarPrecio(Producto p, int nuevoPrecio) throws PrecioInvalidoException {
        p.setPrecio(nuevoPrecio);
    }

    public void cambiarEstadoEnvio(VentaADomicilio v, EstadoEnvio e) throws VentaInvalidaException {
        v.actualizarEstado(e);
    }

    public boolean validarClave(String intento) {
        return clave.equals(intento);
    }
}