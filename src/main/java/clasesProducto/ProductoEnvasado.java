package clasesProducto;

import enums.TipoProducto;
import excepciones.VentaInvalidaException;

public class ProductoEnvasado extends Producto {

    private TipoProducto tipo;

    ///Constructor
    public ProductoEnvasado(String nombre, int precio, int stock, TipoProducto tipo) {
        super(nombre, precio, stock);
        this.tipo = tipo;
    }
    public ProductoEnvasado(){
    }

    ///Gets and Sets
    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    ///Metodos
    @Override
    public double calcularImporte(double cantidad) throws VentaInvalidaException {
        double importe = getPrecio()*cantidad;
        if (cantidad <= 0){
           throw new VentaInvalidaException("El cantidad debe ser mayor a 0");
       }
       if (tipo == TipoProducto.PALITO_BOMBON){
           return importe + 500;
       }
       if (tipo == TipoProducto.ESCOCES || tipo == TipoProducto.SUIZO){
           return importe + 800;
       }
        return getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return "ProductoEnvasado{" +
                "tipoProducto='" + tipo + '\'' +
                "} " + super.toString();
    }

}
