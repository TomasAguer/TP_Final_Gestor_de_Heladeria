import excepciones.VentaInvalidaException;

public class DetalleVenta {

    private Producto producto;
    private double cantidad;
    private double precioUnitario; //Se guarda aparte por si despues cambia el precio del producto

    ///Constructor
    public DetalleVenta(Producto producto, double cantidad) {
        if (producto == null) {
            throw new VentaInvalidaException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new VentaInvalidaException("La cantidad tiene que ser mayor a 0");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    ///Gets and Sets
    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    ///Metodos
    //Subtotal de la linea = cantidad * precio unitario
    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "producto=" + producto.getNombre() +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }

}
