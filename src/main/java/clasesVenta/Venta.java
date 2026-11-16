package clasesVenta;

import clasesProducto.Producto;
import clasesProducto.Sabor;
import clasesUsuario.Cliente;
import excepciones.StockInsuficienteException;
import excepciones.VentaInvalidaException;
import interfaz.Identificable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta implements Identificable {
    private static int contador = 1;

    private int id;
    private LocalDateTime fecha;
    private String metodoPago;
    private double total;
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private boolean confirmada;

    public Venta(String metodoPago, Cliente cliente) {
        this.id = contador++;
        this.fecha = LocalDateTime.now();
        this.metodoPago = metodoPago;
        this.cliente = cliente;
        this.total = 0;
        this.detalles = new ArrayList<>();
        this.confirmada = false;
    }
    public Venta(int id, LocalDateTime fecha, String metodoPago, double total, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.total = total;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.confirmada = true;
    }

    @Override
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

    public Cliente getCliente() {
        return cliente;
    }

    public double getTotal() {
        return total;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    //Protected para que VentaADomicilio pueda sumarle el envio
    protected void setTotal(double total) {
        this.total = total;
    }

    //Devuelve una copia para que no se modifique la lista desde afuera
    public List<DetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);
    }

    //Crea el detalle adentro de la venta, validando que haya stock
    public void agregarDetalle(Producto p, double cantidad, List<Sabor> sabores) throws StockInsuficienteException, VentaInvalidaException {
        if (confirmada) {
            throw new VentaInvalidaException("La venta ya fue confirmada, no se pueden agregar productos");
        }
        if (p != null && cantidadSolicitada(p) + cantidad > p.getStock()) {
            throw new StockInsuficienteException("No hay stock suficiente de " + p.getNombre());
        }
        detalles.add(new DetalleVenta(p, cantidad, sabores));
    }

    //Suma cuanto se pidio ya de ese producto en esta venta
    private double cantidadSolicitada(Producto p) {
        double suma = 0;
        for (DetalleVenta d : detalles) {
            if (d.getProducto().getId() == p.getId()) {
                suma += d.getCantidad();
            }
        }
        return suma;
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
    public void confirmarVenta() throws VentaInvalidaException {
        if (confirmada) {
            throw new VentaInvalidaException("La venta esta confirmada");
        }
        if (detalles.isEmpty()) {
            throw new VentaInvalidaException("No se puede confirmar una venta sin productos");
        }
        for (DetalleVenta d : detalles) {
            d.getProducto().actualizarStock(-d.getCantidad());
        }
        this.fecha = LocalDateTime.now();
        calcularTotal();
        this.confirmada = true;
        if(cliente != null) {
            cliente.agregarVentaAlHistorial(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Venta #%d | Fecha: %s | Pago: %s | Total: $%.2f | Items: %d",
                id, fecha.toLocalDate(), metodoPago, total, detalles.size());
    }

}
