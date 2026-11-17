package clasesProducto;

import enums.TipoProducto;
import excepciones.VentaInvalidaException;

public class ProductoEnvasado extends Producto {

    private TipoProducto tipo;

    ///Constructor
    public ProductoEnvasado(String nombre, double precio, int stock, TipoProducto tipo) {
        super(nombre, precio, stock);
        this.tipo = tipo;
    }

    public ProductoEnvasado(){
    }

    ///Constructor para la base de datos
    public ProductoEnvasado(int id, String nombre, double precio, int stock, TipoProducto tipo) {
        super(id, nombre, precio, stock);
        this.tipo = tipo;
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
    public double calcularImporte(double cantidad)  {

        if (cantidad <= 0){
           throw new IllegalArgumentException("El cantidad debe ser mayor a 0");
       }
        return  tipo.getPrecio()*cantidad;
    }

    @Override
    public String toString() {
        return "ProductoEnvasado{" +
                "tipoProducto='" + tipo + '\'' +
                "} " + super.toString();
    }

}
