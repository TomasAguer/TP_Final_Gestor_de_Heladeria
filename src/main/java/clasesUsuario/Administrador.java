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

    public void actualizarPrecio(Producto p, double nuevoPrecio) throws PrecioInvalidoException {
        if (p == null) {
            throw new IllegalArgumentException("Hay que indicar un producto");
        }
        p.setPrecio(nuevoPrecio);
    }

    public void cambiarEstadoEnvio(VentaADomicilio v, EstadoEnvio e) throws VentaInvalidaException {
       if(v == null){
           throw new VentaInvalidaException("La venta a domicilio no puede ser nula");
       }
        v.actualizarEstado(e);

    }

    public boolean validarClave(String intento) {
        return clave.equals(intento);
    }
}