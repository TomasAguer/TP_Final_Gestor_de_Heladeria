package clasesVenta;

import clasesProducto.Producto;
import clasesProducto.Sabor;
import excepciones.VentaInvalidaException;

import java.util.List;

public class DetalleVenta {

    private Producto producto;
    private double cantidad;
    private double precioUnitario; //Se guarda aparte por si despues cambia el precio del producto
    private List<Sabor> sabores;

    public DetalleVenta(Producto producto, double cantidad, List<Sabor> sabores) throws VentaInvalidaException {
        if (producto == null) {
            throw new VentaInvalidaException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new VentaInvalidaException("La cantidad tiene que ser mayor a 0");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.sabores = sabores;
    }

    //Constructor para la base de datos: usa el precio que se guardo al momento de la venta
    public DetalleVenta(Producto producto, double cantidad, double precioUnitario, List<Sabor> sabores) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.sabores = sabores;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public List<Sabor> getSabores() {
        return sabores;
    }

    public double calcularSubtotal() {
        return producto.calcularImporte(cantidad);
    }

    @Override
    public String toString() {
        //Si la cantidad es entera muestro "2" en vez de "2.0"
        String cant;
        if (cantidad == (int) cantidad) {
            cant = String.valueOf((int) cantidad);
        } else {
            cant = String.valueOf(cantidad);
        }

        String texto = String.format("  - %s x %s ($%.2f c/u) -> $%.2f",
                cant, producto.getNombre(), precioUnitario, calcularSubtotal());

        if (sabores != null && !sabores.isEmpty()) {
            texto += "\n      Sabores: ";
            for (int i = 0; i < sabores.size(); i++) {
                if (i > 0) {
                    texto += ", ";
                }
                texto += sabores.get(i).getNombre();
            }
        }
        return texto;
    }

}
