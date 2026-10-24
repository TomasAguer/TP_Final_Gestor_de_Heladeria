import excepciones.StockInsuficienteException;
import excepciones.VentaInvalidaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private static int contador = 1;

    private int id;
    private LocalDateTime fecha;
    private String metodoPago;
    private double total;
    private List<DetalleVenta> detalles; //Composicion: los detalles no existen sin la venta

    ///Constructor
    public Venta(String metodoPago) {
        this.id = contador++;
        this.fecha = LocalDateTime.now();
        this.metodoPago = metodoPago;
        this.total = 0;
        this.detalles = new ArrayList<>();
    }

    ///Gets and Sets
    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return total;
    }

    //Protected para que VentaADomicilio pueda sumarle el envio
    protected void setTotal(double total) {
        this.total = total;
    }

    //Devuelve una copia para que no se modifique la lista desde afuera
    public List<DetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);
    }

    ///Metodos
    //Crea el detalle adentro de la venta, validando que haya stock
    public void agregarDetalle(Producto p, double cantidad) {
        if (p != null && cantidad > p.getStock()) {
            throw new StockInsuficienteException("No hay stock suficiente de " + p.getNombre());
        }
        detalles.add(new DetalleVenta(p, cantidad));
    }

    //Suma los subtotales de todos los detalles
    public double calcularTotal() {
        double suma = 0;
        for (DetalleVenta d : detalles) {
            suma += d.calcularSubtotal();
        }
        this.total = suma;
        return total;
    }

    //Cierra la venta: al menos un detalle y se descuenta el stock
    public void confirmarVenta() {
        if (detalles.isEmpty()) {
            throw new VentaInvalidaException("No se puede confirmar una venta sin productos");
        }
        for (DetalleVenta d : detalles) {
            Producto p = d.getProducto();
            p.setStock(p.getStock() - (int) Math.ceil(d.getCantidad()));
        }
        this.fecha = LocalDateTime.now();
        calcularTotal();
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", metodoPago='" + metodoPago + '\'' +
                ", total=" + total +
                ", detalles=" + detalles +
                '}';
    }

}
