import enums.EstadoEnvio;
import excepciones.VentaInvalidaException;
import interfaz.EnvioADomicilio;

public class VentaADomicilio extends Venta implements EnvioADomicilio {

    private static final double COSTO_ENVIO_BASE = 1500;
    private static final double MONTO_ENVIO_GRATIS = 50000; //A partir de este monto el envio es gratis

    private String direccionEntrega;
    private double costoEnvio;
    private EstadoEnvio estado;

    ///Constructor
    public VentaADomicilio(String metodoPago, String direccionEntrega) {
        super(metodoPago);
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new VentaInvalidaException("La direccion de entrega es obligatoria");
        }
        this.direccionEntrega = direccionEntrega;
        this.costoEnvio = 0;
        this.estado = EstadoEnvio.PENDIENTE; //Toda venta a domicilio arranca pendiente
    }

    ///Gets and Sets
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    @Override
    public EstadoEnvio getEstado() {
        return estado;
    }

    ///Metodos
    //Al total de los productos le suma el costo de envio
    @Override
    public double calcularTotal() {
        double totalFinal = super.calcularTotal() + calcularCostoEnvio();
        setTotal(totalFinal);
        return totalFinal;
    }

    //Envio fijo, salvo que la compra supere el monto de envio gratis
    @Override
    public double calcularCostoEnvio() {
        double totalProductos = 0;
        for (DetalleVenta d : getDetalles()) {
            totalProductos += d.calcularSubtotal();
        }

        if (totalProductos >= MONTO_ENVIO_GRATIS) {
            costoEnvio = 0;
        } else {
            costoEnvio = COSTO_ENVIO_BASE;
        }
        return costoEnvio;
    }

    //Si el envio ya fue entregado o cancelado no se puede cambiar mas
    @Override
    public void actualizarEstado(EstadoEnvio nuevo) {
        if (nuevo == null) {
            throw new VentaInvalidaException("El estado no puede ser nulo");
        }
        if (estado == EstadoEnvio.ENTREGADO || estado == EstadoEnvio.CANCELADO) {
            throw new VentaInvalidaException("El envio ya esta " + estado + ", no se puede modificar");
        }
        this.estado = nuevo;
    }

    @Override
    public String toString() {
        return "VentaADomicilio{" +
                "direccionEntrega='" + direccionEntrega + '\'' +
                ", costoEnvio=" + costoEnvio +
                ", estado=" + estado +
                "} " + super.toString();
    }

}
